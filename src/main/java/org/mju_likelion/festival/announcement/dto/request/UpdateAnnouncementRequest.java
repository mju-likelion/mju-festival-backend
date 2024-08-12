package org.mju_likelion.festival.announcement.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.mju_likelion.festival.announcement.util.deserializer.UpdateAnnouncementRequestDeserializer;
import org.mju_likelion.festival.common.util.field_wrapper.FieldWrapper;

/**
 * 공지사항 수정 요청 DTO
 * <p>
 * PATCH 를 처리하기 위해 FieldWrapper 를 사용한다.
 */
@Getter
@Setter
@JsonDeserialize(using = UpdateAnnouncementRequestDeserializer.class)
public class UpdateAnnouncementRequest {

  private FieldWrapper<String> title;
  private FieldWrapper<String> content;
  private FieldWrapper<String> imageUrl;
}
