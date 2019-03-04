package io.costax.revisiting.functional;

public class BeforeJdk8TemplateBusinessLogicImplementation extends BeforeJdk8TemplateBusinessLogic {
    public static void main(String[] args) {
        new BeforeJdk8TemplateBusinessLogicImplementation().doIt();
    }

    @Override
    void doSomeThing() {
        System.out.println("===> BeforeJdk8TemplateBusinessLogicImplementation");
    }
}
