/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author Huiyan
 */
import database.ConnectionManager;
import entity.Tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 *
 * @author JeremyBachtiar
 */
public class TagDAO {

    public String addTag(Tag tag) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO TAG VALUES (?,?)";
        if (getTagById(tag.getTagId()) != null) {

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, tag.getTagId());
                stmt.setString(2, tag.getTagName());

                rs = stmt.executeQuery();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        } else {
            return "Tag already exist";
        }

        return "Success";
    }

    public Tag getTagById(int tagId) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Tag result = null;

        String sql = "SELECT * FROM TAG WHERE TAG_ID = ?";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, tagId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String tagName = rs.getString("TAG_NAME");

                result = new Tag(tagId, tagName);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return result;

    }

    public Tag[] getAllTags() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Tag> tagList = new ArrayList<Tag>();

        String sql = "SELECT * FROM TAG";
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int tagId = rs.getInt("TAG_ID");
                String tagName = rs.getString("TAG_NAME");

                Tag tag = new Tag(tagId, tagName);
                tagList.add(tag);

            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return tagList.toArray(new Tag[tagList.size()]);

    }

    public Tag[] getTagsByPatternId(int patternId) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Tag> tagList = new ArrayList<Tag>();

        String sql = "SELECT * FROM TAG T, PATTERN_TAG DT WHERE DT.PATTERN_ID=? AND DT.TAG_ID=T.TAG_ID";
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patternId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int tagId = rs.getInt("TAG_ID");
                String tagName = rs.getString("TAG_NAME");

                Tag tag = new Tag(tagId, tagName);
                tagList.add(tag);

            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return tagList.toArray(new Tag[tagList.size()]);

    }

    public String deleteTagById(int id) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM TAG WHERE TAG_ID = ?";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }

    public String updateTag(Tag tag) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE TAG SET TAG_NAME = ? WHERE TAG_ID = ?";

        if (getTagById(tag.getTagId()) != null) {

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, tag.getTagName());
                stmt.setInt(2, tag.getTagId());

                rs = stmt.executeQuery();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        } else {
            return "Tag does not exist";
        }

        return "Success";
    }

}
