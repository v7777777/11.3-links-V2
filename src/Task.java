import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.RecursiveAction;


public class Task extends RecursiveAction {

    private Page page;

    public Task(Page page)  {
        this.page = page;

    }

    @Override
    protected void compute() {

        String link = page.getLink();   //   https://skillbox.ru/

        try {
            getLinksOnThisPage(link); // заполнить чилдрен корневога узла = метод гет три для этого рут
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Task> taskList = new ArrayList<>();

        for(Page page: page.getChildren()) {   // создать задачу (компьют) каждого ребенка

            Task task = new Task(page);

            task.fork(); //System.out.println("-----------> fork " + task.page.getLink()  + "  " + Thread.currentThread().getName() );
            taskList.add(task);

        }

        for (Task task: taskList ) { task.join();
          //  System.out.println("-----------> join " + task.page.getLink()  + "  " + Thread.currentThread().getName() );
        }


    }




    public void getLinksOnThisPage(String startPage) throws IOException {

        if (Page.getAllLinks().contains(startPage)) {return;}

        if (startPage.contains(".pdf"))
        {Page.getAllLinks().add(startPage);

//        synchronized (Page.file) {   // Page.file   Page.class
//
//                Page.writer.write(startPage + "\n");
//                Page.writer.flush();
            //    System.out.println(startPage);
//            }

            return;}

        if (startPage.contains("#")) {return;}

        Page.getAllLinks().add(startPage);  // <-----------------------------------------

//        synchronized (Page.file) {   // Page.file   Page.class
//
//            Page.writer.write(startPage + "\n");
//            Page.writer.flush();
          //  System.out.println(startPage);
//        }


        Document doc = null;

        try {
             Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {doc = Jsoup.connect(startPage).ignoreHttpErrors(true).timeout(0).get(); }

        catch (Exception e ) { e.printStackTrace();}


        Elements links = doc.select("a[href]");

        for(Element link : links){

            String stringLink = link.attr("abs:href");  //  String stringLink = link.absUrl("href");

            if (stringLink.contains(RootPage.rootLink) ) {  // // https://lenta.ru/ или  https://skillbox.ru/

                if (!Page.visitedLinks.contains(stringLink)) { // чтобы не создавать task на одинакоые страницы

                page.getChildren().add(new Page (stringLink));
                Page.visitedLinks.add(stringLink); }   // после этого уже не добавлять эту стр в детей других страниц

            }


        }



    }



}



