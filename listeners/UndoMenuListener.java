package com.javarush.task.task32.task3209.listeners;

import com.javarush.task.task32.task3209.View;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

//Этот слушатель будет следить за меню, а если конкретнее, то за моментом, когда меню редактирования будет выбрано пользователем.
//В этот момент он будет запрашивать у представления можем ли мы сейчас отменить или вернуть какое-то действие,
// и в зависимости от этого делать доступными или не доступными пункты меню "Отменить" и "Вернуть".
public class UndoMenuListener implements MenuListener {
    private View view;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    public UndoMenuListener(View view, JMenuItem undoMenuItem, JMenuItem redoMenuItem){
        this.view = view;
        this.undoMenuItem = undoMenuItem;
        this.redoMenuItem = redoMenuItem;
    }

    //0.3. Реализуй метод menuSelected(MenuEvent menuEvent). Он будет вызываться перед показом меню. Он должен:
    //10.3.1. Спрашивать у представления можем ли мы отменить действие с помощью метода boolean canUndo().
    //Пока у представления нет такого метода, поэтому добавь заглушку, которая всегда возвращает false.
    //10.3.2. Делать доступным или не доступным пункт меню undoMenuItem в зависимости от того, что нам вернуло представление.
    //
    //Подсказка: используй метод setEnabled().
    //7. Метод menuSelected(MenuEvent menuEvent) должен делать доступными или не доступными пункты меню undoMenuItem и redoMenuItem.
    @Override
    public void menuSelected(MenuEvent e) {
        if(view.canUndo()){
            undoMenuItem.setEnabled(true);
        }
        else{
            undoMenuItem.setEnabled(false);
        }

        if(view.canRedo()){
            redoMenuItem.setEnabled(true);
        }
        else{
            redoMenuItem.setEnabled(false);
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
