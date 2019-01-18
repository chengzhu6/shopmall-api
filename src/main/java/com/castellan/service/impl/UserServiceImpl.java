package com.castellan.service.impl;

import com.castellan.common.Const;
import com.castellan.common.ServerResponse;
import com.castellan.common.TokenCache;
import com.castellan.dao.UserMapper;
import com.castellan.pojo.User;
import com.castellan.service.IUserService;
import com.castellan.util.MD5Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public ServerResponse login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在！");
        }

        User user = userMapper.selectLogin(username, MD5Util.getMD5(password));
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误!");
        }

        // 将登录的token保存到数据库中
        userMapper.updateTokenByUserId(MD5Util.getMD5(user.getUsername() + user.getPassword()), user.getId());
        user.setPassword(null);


        return ServerResponse.createBySuccess(user,"登录成功！");
    }


    @Override
    public ServerResponse checkVaild(String str, String type) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(str)){
            if (Const.TYPE_USERNAME.equals(type) || Const.TYPE_EMAIL.equals(type)){
                if(Const.TYPE_USERNAME.equals(type)){
                    int resultCount = userMapper.checkUsername(str);
                    if (resultCount > 0){
                        return ServerResponse.createByErrorMessage("用户名已存在");
                    }
                }

                if(Const.TYPE_EMAIL.equals(type)){
                    int resultCount = userMapper.checkEmail(str);
                    if (resultCount > 0){
                        return ServerResponse.createByErrorMessage("邮箱已存在");
                    }
                }
            } else {
                return ServerResponse.createByErrorMessage("参数错误");
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse register(User user) {
        ServerResponse vaildResponse = this.checkVaild(user.getUsername(),Const.TYPE_USERNAME);
        if (!vaildResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户名已存在！");
        }
        vaildResponse = this.checkVaild(user.getEmail(),Const.TYPE_EMAIL);
        if (!vaildResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("邮箱已存在！");
        }
        user.setRole(Const.Role.ROLE_CUSTUMER);

        user.setPassword(MD5Util.getMD5(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> getQuestion(String username){
        ServerResponse vaildResponse = checkVaild(username, Const.TYPE_USERNAME);
        if(vaildResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("该用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);

        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("该用户未设置密码");
    }


    public ServerResponse<String> checkAnswer(String username, String question,String answer){

        if(StringUtils.isBlank(answer)){
            return ServerResponse.createByErrorMessage("答案不能为空");
        }

        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }


        return ServerResponse.createByErrorMessage("答案错误");

    }

    public ServerResponse resetPassword(String username, String passwordNew, String token){
        if(StringUtils.isBlank(token)){
           return ServerResponse.createByErrorMessage("参数错误，请传入token");
        }
        ServerResponse vaildResponse = checkVaild(username, Const.TYPE_USERNAME);
        if(vaildResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("该用户不存在");
        }

        String forgetToken = TokenCache.getKey(username);
        if (StringUtils.equals(token,forgetToken)){

           int count = userMapper.updatePasswordByUsername(username,MD5Util.getMD5(passwordNew));
           if (count > 0){
               return ServerResponse.createBySuccess("密码重置成功！");
           }

        } else{
           return ServerResponse.createByErrorMessage("token错误或者已失效");
        }
        return ServerResponse.createByErrorMessage("重置密码失败");
    }


    public ServerResponse resetPassword(String passwordOld, String passwordNew, User user){
        int resutlCount = userMapper.checkPassword(user.getId(),MD5Util.getMD5(passwordOld));
        if (resutlCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.getMD5(passwordNew));

        int resultCount = userMapper.updateByPrimaryKeySelective(user);

        if (resultCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");

    }

    public ServerResponse updateInfomation(User user){
        // 对用户想要更新的邮箱进行验证，判断邮箱是否存在
        int resultCount = userMapper.checkEmailByUserId(user.getId(),user.getEmail());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0){
            return ServerResponse.createBySuccessMessage("信息更新成功");
        }
        return ServerResponse.createByErrorMessage("信息更新失败");
    }


    public ServerResponse<User> getInfomation(Integer id){
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(null);
        return ServerResponse.createBySuccess(user);
    }

    public ServerResponse<User> getUsers(int pageSize, int pageNum){
        PageHelper.startPage(pageNum,pageSize);
        List<User> userList = userMapper.selectAll();
        PageInfo pageInfo = new PageInfo(userList);

        return ServerResponse.createBySuccess(pageInfo);

    }

    /**
     * 检查用户的身份是否为管理员
     * @param role
     * @return
     */
    public ServerResponse checkAdminRole(Integer role){
        if (role == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();

    }
}
