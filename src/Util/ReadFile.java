package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class ReadFile
{
    private Scanner scanner;
    private static Locale locale = new Locale("en", "US");

    public ReadFile(String fileName)
    {
        try
        {
            File file = new File(fileName);
            if (file.exists())
            {
                scanner = new Scanner(file);
                scanner.useLocale(locale);
                return;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public int readInt()
    {
        return scanner.nextInt();
    }
}
