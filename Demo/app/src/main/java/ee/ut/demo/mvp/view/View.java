package ee.ut.demo.mvp.view;

public interface View {
    void showLoading();
    void showError(String msg);
    void showEmpty();
}
