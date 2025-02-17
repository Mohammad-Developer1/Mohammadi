package ir.projectMohammadi.model.enums.role;

import lombok.Getter;

@Getter
public enum Role {

    TEACHER(0,"استاد"),
    STUDENT(1, "دانشجو"),
    ADMIN(2, "مدیر");


    private final Integer Index;
    private final String title;

    Role(Integer index, String title) {
        Index = index;
        this.title = title;
    }
}
