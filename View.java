package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();     //панель с двумя вкладками:
    private JTextPane htmlTextPane = new JTextPane();       //компонент для визуального редактирования html.
    private JEditorPane plainTextPane = new JEditorPane();  //компонент для редактирования html в виде текста, отображает код html (теги и их содержимое).
    private UndoManager undoManager = new UndoManager();



    //11.4. Добавь в представление поле UndoListener undoListener, проинициализируй его используя undoManager.
    private UndoListener undoListener = new UndoListener(undoManager);


    //9.2. Добавь конструктор класса View. Он должен устанавливать внешний вид и поведение (look and feel) нашего приложения такими же, как это определено в системе.
    //Конструктор не должен кидать исключений, только логировать их с помощью ExceptionHandler.
    //Подсказа: для реализации задания используй класс UIManager.
    public View(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            ExceptionHandler.log(e);
        } catch (InstantiationException e) {
            ExceptionHandler.log(e);
        } catch (IllegalAccessException e) {
            ExceptionHandler.log(e);
        } catch (UnsupportedLookAndFeelException e) {
            ExceptionHandler.log(e);
        }
    }


    //этот метод наследуется от интерфейса ActionListener и будет вызваться при выборе пунктов меню, у которых наше представление указано в виде слушателя событий.
    @Override
    public void actionPerformed(ActionEvent e) {
        //19.1. Получи из события команду с помощью метода getActionCommand(). Это будет обычная строка.
        //По этой строке ты можешь понять какой пункт меню создал данное событие.
        //19.2. Если это команда "Новый", вызови у контроллера метод createNewDocument().
        //В этом пункте и далее, если необходимого метода в контроллере еще нет - создай заглушки.
        //19.3. Если это команда "Открыть", вызови метод openDocument().
        //19.4. Если "Сохранить", то вызови saveDocument().
        //19.5. Если "Сохранить как..." - saveDocumentAs().
        //19.6. Если "Выход" - exit().
        //19.7. Если "О программе", то вызови метод showAbout() у представления.
        //Проверь, что заработали пункты меню Выход и О программе.
        String s = e.getActionCommand();
        switch(s){
            case "Новый":
                controller.createNewDocument();
                break;
            case "Открыть":
                controller.openDocument();
                break;
            case "Сохранить":
                controller.saveDocument();
            case "Сохранить как...":
                controller.saveDocumentAs();
            case "Выход":
                controller.exit();
            case "О программе":
                showAbout();
        }
    }

    //инициализация представления
    //4.3. Реализуй метод init() представления. Он должен:
    //4.3.1. Вызывать инициализацию графического интерфейса initGui().
    //4.3.2. Добавлять слушателя событий нашего окна.
    //       В качестве подписчика создай и используй объект класса FrameListener.
    //       В качестве метода для добавления подписчика используй подходящий метод из класса Window от которого наследуется и наш класс через классы JFrame и Frame.
    //4.3.3. Показывать наше окно. Используй метод setVisible с правильным параметром.
    //На этом этапе приложение при запуске должно показывать окно, которое можно растягивать, разворачивать, закрыть и т.д.

    public void init(){
        initGui();
        FrameListener frameListener = new FrameListener(this);  //подписчик.
        this.addWindowListener(frameListener);
        this.setVisible(true);
    }

    //инициализация меню.
    //метод initMenuBar(). Он делает следующее:
    //9.1.1. Создавать новый объект типа JMenuBar. Это и будет наша панель меню.
    //9.1.2. С помощью MenuHelper инициализировать меню в следующем порядке: Файл, Редактировать, Стиль, Выравнивание, Цвет, Шрифт и Помощь.
    //9.1.3. Добавлять в верхнюю часть панели контента текущего фрейма нашу панель меню, аналогично тому, как это мы делали с панелью вкладок.
    //3. В методе initMenuBar() должно добавляться новосозданное меню в верхнюю часть панели контента текущего фрейма, используя метод getContentPane().
    public void initMenuBar(){
        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this, jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this, jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);

        getContentPane().add(jMenuBar, BorderLayout.NORTH);

    }

    //инициализация панелей редактора.
    //Реализуй метод инициализации панелей редактора initEditor(). Он должен:
    //+++6.1. Устанавливать значение "text/html" в качестве типа контента для компонента htmlTextPane.
    //Найди и используй подходящий метод.
    //+++6.2. Создавать новый локальный компонент JScrollPane на базе htmlTextPane.
    //+++6.3. Добавлять вкладку в панель tabbedPane с именем "HTML" и компонентом из предыдущего пункта.
    //+++6.4. Создавать новый локальный компонент JScrollPane на базе plainTextPane.
    //+++6.5. Добавлять еще одну вкладку в tabbedPane с именем "Текст" и компонентом из предыдущего пункта.
    //+++6.6. Устанавливать предпочтительный размер панели tabbedPane.
    //+++6.7. Создавать объект класса TabbedPaneChangeListener и устанавливать его в качестве слушателя изменений в tabbedPane.
    //+++6.8. Добавлять по центру панели контента текущего фрейма нашу панель с вкладками.
    //+++Получить панель контента текущего фрейма можно с помощью метода getContentPane(), его реализация унаследовалась от JFrame.
    //+++Подумай, метод с какими параметрами необходимо вызвать, чтобы панель с вкладками отображалась по центру панели контента текущего фрейма.
    //После запуска приложения можно будет увидеть текущие результаты: две независимые закладки (HTML и Текст), в каждой из которых можно набирать свой текст.
    public void initEditor(){
        htmlTextPane.setContentType("text/html");
        JScrollPane jScrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", jScrollPane);
        JScrollPane jScrollPane2 = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Текст", jScrollPane2);
        tabbedPane.setPreferredSize(new Dimension(640, 480));
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    // инициализация графического интерфейса
    //Вызови из него инициализацию меню initMenuBar(), инициализацию редактора initEditor() и метод pack(), реализацию которого мы унаследовали от класса;
    public void initGui(){
        initMenuBar();
        initEditor();
        pack();
    }

    //вызывается, когда произошла смена выбранной вкладки.
    //18.1. Метод должен проверить, какая вкладка сейчас оказалась выбранной.
    //18.2. Если выбрана вкладка с индексом 0 (html вкладка), значит нам нужно получить текст из plainTextPane и установить его в контроллер с помощью метода setPlainText.
    //18.3. Если выбрана вкладка с индексом 1 (вкладка с html текстом), то необходимо получить текст у контроллера с помощью метода getPlainText() и установить его в панель plainTextPane.
    //18.4. Сбросить правки (вызвать метод resetUndo представления).
    //1. Метод selectedTabChanged() должен проверить, какая вкладка сейчас оказалась выбранной.
    //2. Если индекс вкладки равен 0 - метод selectedTabChanged() должен получить текст из plainTextPane и установить его в контроллер с помощью метода setPlainText().
    //3. Если индекс вкладки равен 1 - метод selectedTabChanged() должен получить текст у контроллера с помощью метода getPlainText() и установить его в панель plainTextPane.
    //4. Метод selectedTabChanged() должен сбросить правки.
    public void selectedTabChanged(){
        if (tabbedPane.getSelectedIndex() == 0){
            controller.setPlainText(plainTextPane.getText());
        }else if(tabbedPane.getSelectedIndex() == 1){
            plainTextPane.setText(controller.getPlainText());

        }
        resetUndo();
    }

    //11.5.3. Реализуй методы boolean canUndo() и boolean canRedo() используя undoManager.
    public boolean canUndo(){
        return undoManager.canUndo();
    }

    public boolean canRedo(){
        return undoManager.canRedo();
    }

    //11.5. Добавь в представление методы:
    //11.5.1. void undo() - отменяет последнее действие. Реализуй его используя undoManager.
    //Метод не должен кидать исключений, логируй их.

    public void undo(){
        try {
            undoManager.undo();
        }catch(CannotUndoException e){
            ExceptionHandler.log(e);
        }
    }

    public void redo(){
        try {
            undoManager.redo();
        }
        catch(CannotRedoException e){
            ExceptionHandler.log(e);
        }
    }

    //11.5.5. Добавь и реализуй метод void resetUndo(), который должен сбрасывать все правки в менеджере undoManager.
    public void resetUndo(){
        undoManager.discardAllEdits();
    }

    //13.1. Добавь в представление метод boolean isHtmlTabSelected().
    //Он должен возвращать true, если выбрана вкладка, отображающая html в панели вкладок (подсказка: ее индекс 0).
    public boolean isHtmlTabSelected(){
        return tabbedPane.getSelectedIndex() == 0;
    }

    //14.1. Добавь в класс представления метод selectHtmlTab(). Он должен:
    //14.1.1. Выбирать html вкладку (переключаться на нее).
    //14.1.2. Сбрасывать все правки с помощью метода, который ты реализовал ранее.
    public void selectHtmlTab(){
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    //14.3. Добавь в представление метод update(), который должен получать документ у контроллера и устанавливать его в панель редактирования htmlTextPane.
    public void update(){
        HTMLDocument document = controller.getDocument();
        htmlTextPane.setDocument(document);
    }

    //14.4. Добавь в представление метод showAbout(),
    // который должен показывать диалоговое окно с информацией о программе.
    // Информацию придумай сам, а вот тип сообщения должен быть JOptionPane.INFORMATION_MESSAGE.
    public void showAbout(){
//        JOptionPane jOptionPane = new JOptionPane();
//        jOptionPane.setMessage("Программа для редактирования HTML.\n Версия 1.0");
        Object message = "Программа для редактирования HTML.\\n Версия 1.0";
        JOptionPane.showMessageDialog(tabbedPane, message, "о программе:", JOptionPane.INFORMATION_MESSAGE);
    }


    //Добавь в представление метод exit(), он должен вызывать exit() у контроллера.
    public void exit(){
        controller.exit();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

}
