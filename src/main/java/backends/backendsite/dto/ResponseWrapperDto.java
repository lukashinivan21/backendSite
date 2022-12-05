package backends.backendsite.dto;

import java.util.List;

public class ResponseWrapperDto<T> {

    private Integer count;
    private List<T> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
