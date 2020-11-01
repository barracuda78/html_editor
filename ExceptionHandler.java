package com.javarush.task.task32.task3209;

public class ExceptionHandler {
    //Класс ExceptionHandler должен содержать метод public static void log(Exception e).
    public static void log(Exception e){
        //Метод log(Exception e) должен выводить в консоль краткое описание проблемы.
        //Пока добавь в него статический метод log(Exception e), который будет выводить в консоль краткое описание проблемы
        // (используй метод toString у переданного исключения).
        System.out.println(e.toString());
    }
}
