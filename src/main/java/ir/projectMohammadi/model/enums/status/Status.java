package ir.projectMohammadi.model.enums.status;

public enum Status {


    ACCEPTED(0,"تایید"),
    PENDING(1, "در حال برسی"),
    REJECT(2, "رد");


    private final Integer Index;
    private final String title;

    Status(Integer index, String title) {
        Index = index;
        this.title = title;
    }
}
