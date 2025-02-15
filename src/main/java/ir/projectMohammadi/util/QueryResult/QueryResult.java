package ir.projectMohammadi.util.QueryResult;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class QueryResult<T> {

    private int pageNumber;

    private int totalRecords;

    private int pageSize;

    @Getter
    private List<T> entityList = new ArrayList<T>();

    public QueryResult() {
    }

    public QueryResult(int pageNumber, int totalRecords, int pageSize, List<T> entityList) {
        this.pageNumber = pageNumber;
        this.totalRecords = totalRecords;
        this.pageSize = pageSize;
        this.entityList = entityList;
    }

}
