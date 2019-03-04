package io.costax.revisiting.functional;

public abstract class BeforeJdk8TemplateBusinessLogic {

    public void doIt() {
        System.out.println("--- A ");
        System.out.println("--- B ");
        System.out.println("--- -- ");

        doSomeThing();

        System.out.println("--- -- ");
        System.out.println("--- Y ");
        System.out.println("--- Z ");
    }

    abstract void doSomeThing();

}
