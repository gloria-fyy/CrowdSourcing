package com.sysu.crowdsourcing.dao.impl.jdbc;

import com.sysu.crowdsourcing.dao.JudgeTaskDao;
import com.sysu.crowdsourcing.dao.TaskDao;
import com.sysu.crowdsourcing.entity.JudgetaskEntity;
import com.sysu.crowdsourcing.tools.DBHelper;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by zhengshouzi on 2015/9/7.
 */
public class JudgeTaskDaoImpl implements JudgeTaskDao {

    @Resource(name = "taskDao")
    TaskDao taskDao;


    public boolean addJudgeTask(JudgetaskEntity judgetaskEntity) {
        boolean b = false;
        Connection connection = DBHelper.getMySqlConnection();
        PreparedStatement ps = null;
        //�������
        String sql = "INSERT INTO judgetask (simple,releaseTime,deadlineTime,completeTime) VALUES(?,?,?,?)";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, judgetaskEntity.getSimple());
            // ps.setTimestamp(2, judgetaskEntity.getReleaseTime());
            // ps.setTimestamp(2, judgetaskEntity.getDeadlineTime());
            //ps.setTimestamp(2, judgetaskEntity.getCompleteTime());

            if (ps.executeUpdate() == 1)
                b = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(connection, ps, null);
        }
        return b;
    }


    public ArrayList<JudgetaskEntity> findAllJudgeTask() {

        ArrayList<JudgetaskEntity> judgeTasks = new ArrayList<JudgetaskEntity>();

        Connection connection = DBHelper.getMySqlConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        //�������е�judgeTask
        String sql = "SELECT * from judgetask ";
        try {
            ps = connection.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                JudgetaskEntity judgetaskEntity = new JudgetaskEntity();
                judgetaskEntity.setId(rs.getInt("id"));
                judgetaskEntity.setSimple(rs.getString("simple"));
                judgetaskEntity.setReleaseTime(rs.getTimestamp("releaseTime"));
                judgetaskEntity.setDeadlineTime(rs.getTimestamp("deadlineTime"));
                judgetaskEntity.setCompleteTime(rs.getTimestamp("completeTime"));

                //Task task = taskDao.findTaskById();
                //judgeTask.setTask(task);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(connection, ps, null);
        }
        return judgeTasks;


    }


    public boolean deleteJudgeTask(JudgetaskEntity judgetaskEntity) {
        return false;
    }


    public boolean updateJudgeUser(JudgetaskEntity judgetaskEntity) {
        return false;
    }

    //�ر�����
    public void close(Connection cn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (cn != null) {
                cn.close();
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
