package com.evan.wj.dao;

import com.evan.wj.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data Access Object 数据访问对象，DAO 用来创建操作数据库的对象，这个对象可以是我们自己开发，也可以是由框架
 * 提供。这里我们通过继承 JpaRespository 的方式来构建 DAO
 *
 * 一般来说，在 DAO 中只定义基础的增删改查操作，而具体的操作则由 Service 完成
 */
public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User getByUsernameAndPassword(String username, String password);
}
