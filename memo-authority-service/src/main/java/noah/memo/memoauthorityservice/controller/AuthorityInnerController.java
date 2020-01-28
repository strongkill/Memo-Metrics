package noah.memo.memoauthorityservice.controller;

import noah.memo.memoauthorityapi.AuthorityApi;
import noah.memo.memoauthorityapi.bean.Account;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 权限服务控制器
 *
 * @author Noah
 * @create 2020-01-28 13:20
 */
@RestController
public class AuthorityInnerController implements AuthorityApi {


    @Override
    public Account getCurrentAccount(@PathVariable(value = "id") Integer id) {

        return null;
    }
}