package com.sysu.crowdsourcing.services.impl;

import com.sysu.crowdsourcing.beans.MailBean;
import com.sysu.crowdsourcing.dao.UserDao;
import com.sysu.crowdsourcing.entity.UserEntity;
import com.sysu.crowdsourcing.exceptions.ServiceException;
import com.sysu.crowdsourcing.services.RegisterService;
import com.sysu.crowdsourcing.tools.MD5Utils;
import com.sysu.crowdsourcing.tools.RegisterTools;
import com.sysu.crowdsourcing.tools.SendMail;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengshouzi on 2015/8/24.
 */
public class RegisterServiceImpl implements RegisterService {

    @Resource(name = "userDao")
    UserDao userDao;
    @Resource(name = "sendMail")
    SendMail sendMail;

    @Override
    public boolean register(UserEntity userEntity) {
        boolean b = false;

        try {
            //���ע��ĸ�����Ϣ
            userEntity.setRegisterDate(new Date());
            userEntity.setStatus(String.valueOf(0));
            userEntity.setActivateCode(MD5Utils.encode2hex(userEntity.getEmail()));

            //�����û���Ϣ
            b = userDao.addUser(userEntity);

            //����ɹ����ͼ����ʼ�
            if (b == true) {
                ///�����ʼ�

                String Content = "<html><head><meta http-equiv='keywords' content='keyword1,keyword2,keyword3'>" +
                        "<meta http-equiv='description' content='this is my page'><meta http-equiv='content-type' content='text/html; charset=utf-8'>" +
                        "</head><body><h1>" + userEntity.getEmail()
                        + "��ã�</h1><h1>     ����������Ӽ����˺ţ�48Сʱ��Ч����������ע���˺ţ�����ֻ��ʹ��һ�Σ��뾡�켤�</br>" +
                        "<a href=http://localhost:8080/MyWeb/register/activate.do?email=" + userEntity.getEmail() + "&validateCode=" + userEntity.getActivateCode() + ">ȥ��֤</a>"
                        + "</h1><h1></h1><h1></h1><h1>  ����һ��ϵͳ�ʼ����벻�ػظ���</h1><h1>   лл��</h1><h1>" + new Date().toString() + "</h1></body></html>";

                Map map = new HashMap();
                map.put("link_address", Content);

                MailBean mailBean = new MailBean();
                mailBean.setToEmail(userEntity.getEmail());
                mailBean.setSubject("�ڰ�������������");
                mailBean.setFrom("601097836@qq.com");
                mailBean.setFromName("֣ǿ");
                mailBean.setData(map);

                System.out.println(mailBean.toString());
                System.out.println(Content);

                try {
                    sendMail.sendHtmlMail(mailBean);
                } catch (Exception e) {
                    //�����ʼ�ʧ�ܣ�ɾ��ע����Ϣ
                    b = false;
                    userDao.deleteUserByEmail(userEntity.getEmail());
                    e.printStackTrace();
                }
            } else {

            }
        } catch (Exception e) {
            b = false;
            e.printStackTrace();
        }

        return b;
    }

    @Override
    public boolean processActivate(String email, String validateCode) throws ServiceException {

        boolean b = false;
        //���ݷ��ʲ㣬ͨ��email��ȡ�û���Ϣ
        UserEntity userEntity = userDao.findUserByEmail(email);
        System.out.println(userEntity.toString());
        //��֤�û��Ƿ����
        if (userEntity != null) {
            //��֤�û�����״̬
            if (userEntity.getStatus().equals("0")) {
                ///û����
                //��ȡ��ǰʱ��
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                //��֤�����Ƿ����
                // currentTime.before(registerForm.getRegisterTime());
                if (currentTime.before(RegisterTools.getLastActivateTime(new Timestamp(userEntity.getRegisterDate().getTime())))) {
                    //��֤�������Ƿ���ȷ
                    if (validateCode.equals(userEntity.getActivateCode())) {
                        //����ɹ��� //�������û��ļ���״̬��Ϊ�Ѽ���
                        System.out.println("==sq===" + userEntity.getStatus());
                        userEntity.setStatus(String.valueOf(1));//��״̬��Ϊ����
                        System.out.println("==sh===" + userEntity.getStatus());
                        userDao.updateRegisterStatus(userEntity.getEmail(), String.valueOf(1));
                        b = true;
                    } else {
                        throw new ServiceException("�����벻��ȷ");
                    }
                } else {
                    throw new ServiceException("�������ѹ��ڣ�");
                }
            } else {
                throw new ServiceException("�����Ѽ�����¼��");
            }
        } else {
            throw new ServiceException("������δע�ᣨ�����ַ�����ڣ���");
        }

        return b;

    }
}
