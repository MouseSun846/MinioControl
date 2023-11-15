package com.example.minio.Util;

/**
 * @author MouseSun
 * @Address 杭州电子科技大学
 * @date 2020/2/7 20:36
 */
public class ResultVOUtil {
    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }
    public static ResultVO success(){
        return success(null);
    }
    public static ResultVO error(Integer code,String msg){
        ResultVO resultVO = new ResultVO<>();
        resultVO.setMsg(msg);
        resultVO.setCode(code);
        return resultVO;
    }

}
