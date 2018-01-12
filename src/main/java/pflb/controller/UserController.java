package pflb.controller;

import com.google.gson.*;
import org.springframework.web.bind.annotation.*;
import pflb.entity.Session;
import pflb.entity.UserForLogin;
import pflb.entity.UserInfo;

import java.sql.*;

import static pflb.db.Connection.getConnection;

@RestController
public class UserController {

    final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private int ResultCode;
    private String sql;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public String postAuthUser(@RequestBody String JsonReq) throws JsonParseException {

        sql = "{CALL dbo.LogIn(?,?)}";
        Connection con = null;
        con = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sessionIDJson = "";

        try {

            UserForLogin user = GSON.fromJson(JsonReq, UserForLogin.class);

            pstmt = con.prepareCall(sql);
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassMD5());
            rs = pstmt.executeQuery();
            rs.next();

            Session sessionReq = new Session(rs.getString("SESSION_ID"));
            sessionIDJson = GSON.toJson(sessionReq);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {rs.close();} catch (SQLException ignored) {}
            try {pstmt.close();} catch (SQLException ignored) {}
            try {con.close();} catch (SQLException ignored) {}
        }
        return sessionIDJson;
    }

    @RequestMapping(value = "/user/session/{id}", method = RequestMethod.GET)
    public String getUserInfo(@PathVariable String sessionID) throws JsonParseException{

        sql = "{CALL dbo.GetUser(?)}";
        Connection con = null;
        con = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String infoReq = "";

        try {
            pstmt = con.prepareCall(sql);
            pstmt.setString(1,sessionID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return infoReq;
    }

    @RequestMapping(value = "/auth/session/{login}", method = RequestMethod.DELETE)
    public int deleteSession(@PathVariable String login){

        sql = "{CALL dbo.LogOut(?)}";
        Connection con = null;
        con = getConnection();
        PreparedStatement pstmt = null;

        System.out.println(login);

        try {
            pstmt = con.prepareCall(sql);
            pstmt.setString(1, login);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            ResultCode = 1;
        } finally {
            try {pstmt.close();} catch (SQLException ignored) {}
            try {con.close();} catch (SQLException ignored) {}
        }

        return ResultCode;
    }


}
