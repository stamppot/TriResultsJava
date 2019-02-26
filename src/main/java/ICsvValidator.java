import java.util.List;

public interface ICsvValidator {
    boolean isEmpty(List<List<String>> csvValues);

    boolean isValid(List<List<String>> csvValues);

    List<String> getErrors();
}
