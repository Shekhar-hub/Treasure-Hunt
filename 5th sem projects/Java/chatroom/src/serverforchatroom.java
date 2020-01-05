
import com.vmm.JHTTPServer;
import static com.vmm.NanoHTTPD.HTTP_OK;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import pack.DBLoader;

public class serverforchatroom extends JHTTPServer {

    public serverforchatroom(int port) throws IOException {
        super(port);
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        Response res = null;
        if (uri.contains("/AdminSignup")) {
            String username = parms.getProperty("username");
            String password = parms.getProperty("password");
            try {
                ResultSet rs = DBLoader.executeStatement("select * from adminlogin where username='" + username + "' and password='" + password + "'");
                if (rs.next()) {
                    res = new Response(HTTP_OK, "text/plain", "Login Successful");
                } else {
                    res = new Response(HTTP_OK, "text/plain", "Login Failed");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (uri.contains("/UserSignup")) {
            String name = parms.getProperty("username");
            String password = parms.getProperty("password");
            String email = parms.getProperty("email");
            String phone = parms.getProperty("phonenumber");
            String displayname = parms.getProperty("displayname");

            String filename = "";
            filename = saveFileOnServerWithRandomName(files, parms, "profilepic", "src/uploaded_pics");
            try {
                ResultSet rs = DBLoader.executeStatement("select * from usersignup where username='" + name + "'");
                if (rs.next()) {
                    res = new Response(HTTP_OK, "text/plain", "Same user Exists");
                } else {
                    rs.moveToInsertRow();
                    rs.updateString("username", name);
                    rs.updateString("password", password);
                    rs.updateString("email", email);
                    rs.updateString("phone", phone);
                    rs.updateString("displayname", displayname);
                    rs.updateString("profilepic", "src/uploaded_pics/" + filename);
                    rs.insertRow();
                    res = new Response(HTTP_OK, "text/plain", "Signup successful");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (uri.contains("/GetResource")) {
            uri = uri.substring(1);
            uri = uri.substring(uri.indexOf("/") + 1);
            System.out.println(uri);
            res = sendCompleteFile(uri);
        } else if (uri.contains("/UserLogin")) {
            try {
                String username = parms.getProperty("username");
                String password = parms.getProperty("password");
                ResultSet rs = DBLoader.executeStatement("select * from usersignup where username='" + username + "' and password='" + password + "'");
                if (rs.next()) {
                    res = new Response(HTTP_OK, "text/plain", "Login Successful");
                } else {
                    res = new Response(HTTP_OK, "text/plain", "failed");
                }

            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/addroom")) {
            try {
                String roomanme = parms.getProperty("roomname");
                String category = parms.getProperty("category");
                String filename = "";
                filename = saveFileOnServerWithRandomName(files, parms, "photo", "src/uploaded_pics");
                ResultSet rs = DBLoader.executeStatement("select * from rooms where category='" + category + "' and roomname='" + roomanme + "'");
                if (rs.next()) {
                    res = new Response(HTTP_OK, "text/plain", "same room already exists");
                } else {
                    rs.moveToInsertRow();
                    rs.updateString("category", category);
                    rs.updateString("roomname", roomanme);
                    rs.updateString("photo", "src/uploaded_pics/" + filename);
                    rs.insertRow();
                    res = new Response(HTTP_OK, "text/plain", "room added");
                }
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/GetCategories")) {
            String category = parms.getProperty("category");
            String ans = "";
            try {
                ResultSet rs = DBLoader.executeStatement("select * from rooms where category='" + category + "'");
                while (rs.next()) {
                    String rid = rs.getString("rid");
                    String roomname = rs.getString("roomname");
                    String catname = rs.getString("category");
                    String photo = rs.getString("photo");
                    ans = ans + rid + "~~" + roomname + "~~" + catname + "~~" + photo + ";;";
                }
                res = new Response(HTTP_OK, "text/plain", ans);
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/GetNameandPhoto")) {
            String ans = "";
            try {
                String username = parms.getProperty("username");
                System.out.println("server " + username);
                ResultSet rs = DBLoader.executeStatement("select * from usersignup where username='" + username + "'");
                if (rs.next()) {
                    String displayname = rs.getString("displayname");
                    String photo = rs.getString("profilepic");
                    ans = ans + displayname + "~~" + photo;
                    System.out.println("serverans" + ans);
                }
                res = new Response(HTTP_OK, "text/plain", ans);
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/GetRooms")) {

            String ans = "";
            try {
                ResultSet rs = DBLoader.executeStatement("select * from rooms ");
                while (rs.next()) {
                    String rid = rs.getString("rid");
                    String roomname = rs.getString("roomname");
                    String catname = rs.getString("category");
                    String photo = rs.getString("photo");
                    ans = ans + rid + "~~" + roomname + "~~" + catname + "~~" + photo + ";;";
                }
                res = new Response(HTTP_OK, "text/plain", ans);
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/joinroom")) {
            try {
                String username = parms.getProperty("username");
                String rid = parms.getProperty("roomid");
                ResultSet rs = DBLoader.executeStatement("select * from myrooms where username='" + username + "' and rid='" + rid + "'");
                if (rs.next()) {
                    res = new Response(HTTP_OK, "text/plain", "RoomAlready joined");
                } else {
                    rs.moveToInsertRow();
                    rs.updateString("username", username);
                    rs.updateString("rid", rid);
                    rs.insertRow();
                    res = new Response(HTTP_OK, "text/plain", "room joined successfully");
                }
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/getroomdetails")) {
            String ans = "";
            try {
                String roomid = parms.getProperty("roomid");
                ResultSet rs = DBLoader.executeStatement("select * from rooms where rid='" + roomid + "'");
                if (rs.next()) {
                    String roomname = rs.getString("roomname");
                    String category = rs.getString("category");
                    String photo = rs.getString("photo");
                    ans = roomname + "~~" + category + "~~" + photo;
                }
                res = new Response(HTTP_OK, "text/plain", ans);
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/checkjoinroom")) {
            try {
                String username = parms.getProperty("username");
                String rid = parms.getProperty("roomid");
                ResultSet rs = DBLoader.executeStatement("select * from myrooms where username='" + username + "' and rid='" + rid + "'");
                if (rs.next()) {
                    res = new Response(HTTP_OK, "text/plain", "joined");
                } else {
                    res = new Response(HTTP_OK, "text/plain", "not joined");
                }
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/deleteroom")) {
            try {
                String roomid = parms.getProperty("roomid");
                ResultSet rs = DBLoader.executeStatement("select * from rooms where rid='" + roomid + "'");
                if (rs.next()) {
                    rs.deleteRow();
                    res = new Response(HTTP_OK, "text/plain", "deleted successfully");
                } else {
                    res = new Response(HTTP_OK, "text/plain", "failed to delete");
                }
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/myrooms")) {
            String ans = "";
            try {
                String username = parms.getProperty("username");
                ResultSet rs = DBLoader.executeStatement("select * from rooms where rid in(select rid from myrooms where username='" + username + "')");
                while (rs.next()) {
                    String roomid = rs.getString("rid");
                    String roomname = rs.getString("roomname");
                    String category = rs.getString("category");
                    String photo = rs.getString("photo");
                    ans = ans + roomid + "~~" + roomname + "~~" + category + "~~" + photo + ";;";
                }
                res = new Response(HTTP_OK, "text/plain", ans);
            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/AddMessage")) {

            try {
                String username = parms.getProperty("username");
                String message = parms.getProperty("message");
                String rid = parms.getProperty("roomid");
                String msgtype = parms.getProperty("msgtype");

                ResultSet rs = DBLoader.executeStatement("select * from message");
                rs.next();
                rs.moveToInsertRow();
                rs.updateString("rid", rid);
                rs.updateString("postedby", username);
                rs.updateString("message", message);
                rs.updateString("type", msgtype);
                rs.insertRow();
                res = new Response(HTTP_OK, "text/plain", "Added");

            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/fetchmessages")) {
            String ans = "";
            try {
                String rid = parms.getProperty("roomid");
                ResultSet rs = DBLoader.executeStatement("select * from message where rid='" + rid + "'");

                while (rs.next()) {

                    String message = rs.getString("message");
                    String postedby = rs.getString("postedby");
                    String time = rs.getString("datetime");
                    String type = rs.getString("type");
                    ResultSet rs2 = DBLoader.executeStatement("select * from usersignup where username='" + postedby + "'");
                    if (rs2.next()) {
                        String displayname = rs2.getString("displayname");
                        ans = ans + message + "~~" + postedby + "~~" + time + "~~" + displayname + "~~" + type + ";;";

                    }
                }
                res = new Response(HTTP_OK, "text/plain", ans);

            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/getpersons")) {
            String ans = "";
            try {
                String rid = parms.getProperty("roomid");
                ResultSet rs = DBLoader.executeStatement("select * from usersignup where username in(select username from myrooms where rid='" + rid + "')");
                while (rs.next()) {
                    String displayname = rs.getString("displayname");
                    ans = ans + displayname + "~~";
                }

                res = new Response(HTTP_OK, "text/plain", ans);

            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/AddphotoMessage")) {
            String filename = "";
            filename = saveFileOnServerWithRandomName(files, parms, "message", "src/uploaded_pics");
            try {
                String username = parms.getProperty("username");
                String rid = parms.getProperty("roomid");
                String msgtype = parms.getProperty("msgtype");

                ResultSet rs = DBLoader.executeStatement("select * from message");
                rs.next();
                rs.moveToInsertRow();
                rs.updateString("rid", rid);
                rs.updateString("postedby", username);
                rs.updateString("message", "src/uploaded_pics/" + filename);
                rs.updateString("type", msgtype);
                rs.insertRow();
                res = new Response(HTTP_OK, "text/plain", "Added");

            } catch (Exception ex) {
                Logger.getLogger(serverforchatroom.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (uri.contains("/UserChangePassword")) {
            String un = parms.getProperty("username");
            String oldpassword = parms.getProperty("oldpassword");
            String newpassword = parms.getProperty("newpassword");
            try {
                ResultSet rs = DBLoader.executeStatement("select * from usersignup where username='" + un + "' and password='" + oldpassword + "'");
                if (rs.next()) {
                    rs.updateString("password", newpassword);
                    rs.updateRow();
                    res = new Response(HTTP_OK, "text/plain", "Password Changed Successfully");
                } else {
                    res = new Response(HTTP_OK, "text/plain", "Old password is wrong");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }

}
