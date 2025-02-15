package ir.projectMohammadi.web.viewModel.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseViewModel<T> implements Serializable {
    private T id;
}
