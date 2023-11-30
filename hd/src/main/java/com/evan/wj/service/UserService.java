package com.evan.wj.service;

import com.evan.wj.dao.UserDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    RoleService roleService;
    
    /**
     * 查询所有普通用户信息
     * @return
     */
    public List<User> list(PaginationDTO query) {
//        List<User> users = userDAO.findAll();
//        List<User> filterUsers = new ArrayList<>();
//        users.forEach(user -> {
//            if (!"管理员".equals(user.getRolename())) {
//                filterUsers.add(user);
//            }
//        });
//        return filterUsers;
        return null;
    }

    /**
     * 请求指定数据表中指定页码的数据
     * @param page 指定页码
     * @param limit 页面数据条目数量
     * @return 查询符合要求的用户列表
     */
    public PaginationDTO<User> queryByPage(int page, int limit) {
        List<User> users = userDAO.findAll();
        List<User> filterUsers = new ArrayList<>();
        List<User> curPageUserList = new ArrayList<>();
        users.forEach(user -> {
            if (!"admin".equals(user.getRolename())) {
                filterUsers.add(user);
            }
        });
        for (int i = (page - 1) * limit; i < Math.min(limit * page, filterUsers.size()); i++) {
            curPageUserList.add(filterUsers.get(i));
        }
        return new PaginationDTO<>(filterUsers.size(), curPageUserList);
    }

    /**
     * 判断用户是否存在
     * @param username
     * @return
     */
    public boolean isExist(String username) {
        User user = userDAO.findByUsername(username);
        return null != user;
    }
    
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    
    public User get(String username, String password) {
        return userDAO.getByUsernameAndPassword(username, password);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    public int register(User user) {
        String username = user.getUsername();
        String name = user.getName();
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getPassword();
        String rolename = user.getRolename();
        
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        rolename = HtmlUtils.htmlEscape(rolename);
        user.setRolename(rolename);
        user.setEnabled(true);
        // 设置默认
        user.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        
        if (username.equals("") || password.equals("")) {
            return 0;
        }
        
        boolean exist = isExist(username);
        if (exist) {
            return 2;
        }
        
        // 默认生成16位salt
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userDAO.save(user);
        
        return 1;
    }
    
    public void alterUserInfo(User user) {
        // 获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        User userInDB = userDAO.findByUsername(subject.getPrincipal().toString());
        String phone = !"".equals(user.getPhone()) ? user.getPhone() : userInDB.getPhone();
        userInDB.setPhone(phone);
        String email = user.getEmail() != null ? user.getEmail() : userInDB.getEmail();
        userInDB.setEmail(email);
        String sex = user.getSex() != null ? user.getSex() : userInDB.getSex();
        userInDB.setSex(sex);
        String birthday = user.getBirthday() != null ? user.getBirthday() : userInDB.getBirthday();
        userInDB.setBirthday(birthday);
        String occupation = user.getOccupation() != null ? user.getOccupation() : userInDB.getOccupation();
        userInDB.setOccupation(occupation);
        String personSignature = user.getPersonSignature() != null ? user.getPersonSignature() : userInDB.getPersonSignature();
        userInDB.setPersonSignature(personSignature);
        userDAO.save(userInDB);
    }

    /**
     * 密码重置为123
     * @param user
     * @return
     */
    public User resetPassword(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        userInDB.setSalt(salt);
        String encodePassword = new SimpleHash("md5", "123", salt, times).toString();
        userInDB.setPassword(encodePassword);
        return userDAO.save(userInDB);
    }

    /**
     * 编辑个人信息
     * @param user 前端提交的用户信息
     */
    public void editUser(User user) {
        // 获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        User userInDB = userDAO.findByUsername(subject.getPrincipal().toString());
        String phone = !"".equals(user.getPhone()) ? user.getPhone() : userInDB.getPhone(); 
        userInDB.setPhone(phone);
        String email = user.getEmail() != null ? user.getEmail() : userInDB.getEmail();
        userInDB.setEmail(email);
        String sex = user.getSex() != null ? user.getSex() : userInDB.getSex();
        userInDB.setSex(sex);
        String birthday = user.getBirthday() != null ? user.getBirthday() : userInDB.getBirthday();
        userInDB.setBirthday(birthday);
        String occupation = user.getOccupation() != null ? user.getOccupation() : userInDB.getOccupation();
        userInDB.setOccupation(occupation);
        String personSignature = user.getPersonSignature() != null ? user.getPersonSignature() : userInDB.getPersonSignature();
        userInDB.setPersonSignature(personSignature);
        userDAO.save(userInDB);
    }
    
    public void deleteById(int id) {
        userDAO.deleteById(id);
    }

    /**
     * 更新用户头像
     * @param file 上传头像图片
     */
    public String updateAvator(MultipartFile avatar, HttpServletRequest request) throws FileNotFoundException {
        log.info(avatar.getOriginalFilename());
        
        String avatarName = avatar.getOriginalFilename();
        
        if (avatarName == null) {
            return "文件为空！";
        }
        
        if (!avatarName.endsWith(".png") && !avatarName.endsWith(".jpg")) {
            return "文件类型错误！";
        }
        
        // 文件保存的位置
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "static/avatar";
        realPath = realPath.substring(1);
        log.info(realPath);

        File folder = new File(realPath);
        if (!folder.exists()) {
            boolean ret = folder.mkdirs();
            if (!ret) {
                return "存储目录异常！"; 
            }
        }
        
        String newName;
        if (avatarName.endsWith(".png")) {
            newName = UUID.randomUUID() + ".png";
        } else {
            newName = UUID.randomUUID() + ".jpg";
        }
        
        // 新建头像文件
        try {
            avatar.transferTo(new File(folder, newName));
        } catch (IOException e) {
            return e.toString();
        }

        // 将上传头像的URL存入数据库
        Subject subject = SecurityUtils.getSubject();
        User userInDB = userDAO.findByUsername(subject.getPrincipal().toString());
        String avatarURL = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + "/avatar/" + newName;
        userInDB.setAvatar(avatarURL);
        userDAO.save(userInDB);
        
        // 返回可供外界访问的url
        return avatarURL;
    }
}
