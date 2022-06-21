package cn.itcast.hmall.dto.common;

import lombok.Data;

/**
 * 返回结果封装类
 */
@Data
public class ResultDTO {
    private Boolean success;
    private String msg;
    private Object data;
    public static ResultDTO ok(Object data){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData(data);
        resultDTO.setMsg("操作成功");
        resultDTO.setSuccess(true);
        return resultDTO;
    }
    public static ResultDTO ok(){
        return ok(null);
    }
    public static ResultDTO error(String msg){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData(null);
        resultDTO.setMsg(msg);
        resultDTO.setSuccess(false);
        return resultDTO;
    }
    public static ResultDTO error(){
        return error("操作失败");
    }
}
