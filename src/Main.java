
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;


public class Main {

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        Page root =  new Page("https://skillbox.ru/");  // https://lenta.ru/   https://skillbox.ru/

        ForkJoinPool forkJoinPool =  new ForkJoinPool();

        forkJoinPool.invoke(new Task(root));

      //  System.out.println("Link printer");

        LinksPrinter linksPrinter = new LinksPrinter();

        linksPrinter.print(Page.allLinks);

        Page.writer.flush();
        Page.writer.close();

        long stop = System.currentTimeMillis();

        System.out.println(stop-start + " ms -----------");


    }



}


