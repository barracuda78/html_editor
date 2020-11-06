package com.javarush.task.task32.task3209;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    //конструктор класса Controller - должен принимать в качестве параметра представление и инициализировать соответствующее поле класса.
    public Controller(View view){
        this.view = view;
    }

    ////инициализация контроллера
    //20.2. Реализуй метод инициализации init() контроллера.
    //Он должен просто вызывать метод создания нового документа.
    //Проверь работу пункта меню Новый.
    public void init(){
        createNewDocument();
    }

    //Добавь в контроллер метод resetDocument(), который будет сбрасывать текущий документ. Он должен:
    //15.1. Удалять у текущего документа document слушателя правок которые можно отменить/вернуть (найди подходящий для этого метод, унаследованный от AbstractDocument).
    //Слушателя нужно запросить у представления (метод getUndoListener()).
    //Не забудь проверить, что текущий документ существует (не null).
    //15.2. Создавать новый документ по умолчанию и присваивать его полю document.
    //Подсказка: воспользуйся подходящим методом класса HTMLEditorKit.
    //15.3. Добавлять новому документу слушателя правок.
    //15.4. Вызывать у представления метод update().
    //Требования:
    //1. Класс Controller должен содержать публичный метод resetDocument(), который будет сбрасывать текущий документ.
    //2. Метод resetDocument() должен удалять у текущего документа document слушателя правок через метод removeUndoableEditListener().
    //3. Метод resetDocument() должен создавать новый документ по умолчанию через метод createDefaultDocument().
    //4. Метод resetDocument() должен добавлять новому документу слушателя правок через метод addUndoableEditListener().
    //5. Метод resetDocument() должен вызывать у представления метод update().
    public void resetDocument(){
        if(document != null)
            document.removeUndoableEditListener(view.getUndoListener());
        document = (HTMLDocument)new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    //2.3. Добавь в контроллер метод exit(), он должен вызывать статический метод exit у класса System.
    //2.3.1. Метод exit в классе Controller не должен быть статическим.
    public void exit(){
        System.exit(0);
    }

    //записывать переданный текст с html тегами в документ document.
    public void setPlainText(String text){
        //16.1. Сбрось документ.
        //16.2. Создай новый реадер StringReader на базе переданного текста.
        //16.3. Вызови метод read() из класса HTMLEditorKit, который вычитает данные из реадера в документ document.
        //16.4. Проследи, чтобы метод не кидал исключения. Их необходимо просто логировать.
        resetDocument();
        try {
            new HTMLEditorKit().read(new StringReader(text), document, 0 );
        } catch (IOException e) {
            ExceptionHandler.log(e);
        } catch (BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }

    // получать текст из документа со всеми html тегами.
    public String getPlainText(){
        //17.1. Создай объект StringWriter.
        //17.2. Перепиши все содержимое из документа document в созданный объект с помощью метода write класса HTMLEditorKit.
        //17.3. Как обычно, метод не должен кидать исключений.
        StringWriter stringWriter = null;
        try {
            stringWriter = new StringWriter();
            new HTMLEditorKit().write(stringWriter, document, 0, document.getLength());
        } catch (IOException e) {
            ExceptionHandler.log(e);
        } catch (BadLocationException e) {
            ExceptionHandler.log(e);
        }
        return stringWriter.toString();
    }

    // метод создания нового документа
    public void createNewDocument(){
        //20.1.1. Выбирать html вкладку у представления.
        //20.1.2. Сбрасывать текущий документ.
        //20.1.3. Устанавливать новый заголовок окна, например: "HTML редактор". Воспользуйся методом setTitle(), который унаследован в нашем представлении.
        //20.1.4. Сбрасывать правки в Undo менеджере. Используй метод resetUndo представления.
        //20.1.5. Обнулить переменную currentFile.
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        view.resetUndo();
        currentFile = null;
    }

    //Метод должен работать аналогично методу saveDocumentAs(), в той части, которая отвечает за выбор файла.
    //
    //Когда файл выбран, необходимо:
    //23.2.1. Установить новое значение currentFile.
    //23.2.2. Сбросить документ.
    //23.2.3. Установить имя файла в заголовок у представления.
    //23.2.4. Создать FileReader, используя currentFile.
    //23.2.5. Вычитать данные из FileReader-а в документ document с помощью объекта класса HTMLEditorKit.
    //23.2.6. Сбросить правки (вызвать метод resetUndo представления).
    //23.2.7. Если возникнут исключения - залогируй их и не пробрасывай наружу.
    public void openDocument(){

        view.selectHtmlTab();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
        int option = jFileChooser.showOpenDialog(view);
        if (option == JFileChooser.APPROVE_OPTION){
            currentFile = jFileChooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());
            try {
                FileReader fileReader = new FileReader(currentFile);
                new HTMLEditorKit().read(fileReader, document, 0);
                view.resetUndo();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
    }



    //метод для сохранения файла под новым именем
    public void saveDocumentAs(){
        //22.1. Переключать представление на html вкладку.
        //22.2. Создавать новый объект для выбора файла JFileChooser.
        //22.3. Устанавливать ему в качестве фильтра объект HTMLFileFilter.
        //22.4. Показывать диалоговое окно "Save File" для выбора файла.
        //22.5. Если пользователь подтвердит выбор файла:
        //22.5.1. Сохранять выбранный файл в поле currentFile.
        //22.5.2. Устанавливать имя файла в качестве заголовка окна представления.
        //22.5.3. Создавать FileWriter на базе currentFile.
        //22.5.4. Переписывать данные из документа document в объекта FileWriter-а аналогично тому, как мы это делали в методе getPlainText().
        //22.6. Метод не должен кидать исключения.

        view.selectHtmlTab();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
        int option = jFileChooser.showSaveDialog(view);
        if (option == JFileChooser.APPROVE_OPTION){
            currentFile = jFileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }

        }

    }


    //метод для сохранения открытого файла
    public void saveDocument(){
        //Метод должен работать также, как saveDocumentAs(), но не запрашивать файл у пользователя, а использовать currentFile.
        //Если currentFile равен null, то вызывать метод saveDocumentAs().
        //1. Метод saveDocument() в контроллере должен переключать представление на html вкладку.
        //2. Метод saveDocument() в контроллере должен создавать FileWriter на базе currentFile, если currentFile != null.
        //3. Метод saveDocument() в контроллере должен используя HTMLEditorKit переписывать данные из документа document в объект FileWriter-а, если currentFile != null.
        //4. Метод saveDocument() в контроллере должен вызывать метод saveDocumentAs(), если currentFile == null.
        //5. Метод saveDocument() в контроллере не должен кидать исключения, а логировать через ExceptionHandler.
        view.selectHtmlTab();
        if(currentFile != null){
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }else{
            saveDocumentAs();
        }

    }

    //getter
    public HTMLDocument getDocument() {
        return document;
    }




    //2.2.1. Создавать объект представления.
    //2.2.2. Создавать контроллер, используя представление.
    //2.2.3. Устанавливать у представления контроллер.
    //2.2.4. Инициализировать представление.
    //2.2.5. Инициализировать контроллер. Контроллер должен инициализироваться после представления.
    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }


}
