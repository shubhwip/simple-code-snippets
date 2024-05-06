package me.shubhamjain.codesamples.mvp_code_review.objectmutation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Metadata {
    String location;
    String[] options;
}
