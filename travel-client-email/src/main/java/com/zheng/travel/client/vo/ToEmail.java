package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToEmail implements Serializable {

  /**
   * 邮件接收方，可多人
   */
  private String[] tos;
  /**
   * 邮件主题
   */
  private String subject;
  /**
   * 编号 1 注册模板 2：告警模板
   */
  private Integer code;
}
