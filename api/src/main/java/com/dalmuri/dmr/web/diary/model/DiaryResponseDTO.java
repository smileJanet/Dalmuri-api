package com.dalmuri.dmr.web.diary.model;

import com.google.cloud.language.v1.Sentiment;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiaryResponseDTO {

    private Sentiment sentiment;
    private float score;
    private float magnitude;

}
