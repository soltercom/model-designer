package test.view;

interface ViewInit {
    default void init() {
        initNodes();
        initBindings();
    }

    void initNodes();
    void initBindings();
}
