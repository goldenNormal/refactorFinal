import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;



class GlobalTest {
    static void test() throws IOException {
        ArrayList<Integer> al=new ArrayList<>();
        al.add(1);
        al.add(2);
        al.add(3);
        al.add(2);
        al=al.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
        System.out.println(al.toString());
    }

    public static void main(String[] args) throws IOException {
        GlobalTest.test();
    }


}
