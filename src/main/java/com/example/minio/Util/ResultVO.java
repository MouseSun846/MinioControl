package com.example.minio.Util;

import lombok.Data;

/**
 * http请求返回的最外层对象
 * @author MouseSun
 * @Address 杭州电子科技大学
 * @date 2020/2/7 19:15
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> {
    /*错误码*/
    private Integer code;
    /*提示信息*/
    private String msg;
    /*具体内容*/
    private T data;
}
