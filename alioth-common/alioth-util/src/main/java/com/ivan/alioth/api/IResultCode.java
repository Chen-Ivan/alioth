package com.ivan.alioth.api;

import java.io.Serializable;

/**
 * 业务代码接口
 *
 * @author ivan
 */
public interface IResultCode extends Serializable {

	/**
	 * 消息
	 *
	 * @return String
	 */
	String getMessage();

	/**
	 * 状态码
	 *
	 * @return int
	 */
	int getCode();

}
