package com.javarush.task.task32.task3209.listeners;

import com.javarush.task.task32.task3209.View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameListener extends WindowAdapter {
    private View view;

    public FrameListener(View view) {
        this.view = view;
    }

    //Иметь переопределенный метод windowClosing(WindowEvent windowEvent), который должен вызывать exit() у представления.
    @Override
    public void windowClosing(WindowEvent e) {
        view.exit();
    }
}
