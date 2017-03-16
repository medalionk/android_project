package ee.ut.demo.data;

/**
 * Created by Bilal Abdullah on 3/16/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("Hitman555");
        cricket.add("Pannkoogihommik");
        cricket.add("Avarongkäik");
        cricket.add("Tudengite öölaulupidu");
        cricket.add("Öölaulupeo järelpidu");

        List<String> football = new ArrayList<String>();
        football.add("Kastironimise eelvoor");
        football.add("Mälukas");
        football.add("Pubiralli");
        football.add("Tantsumaraton");
        football.add("Pannkoogihommik");

        List<String> basketball = new ArrayList<String>();
        basketball.add("Avarongkäik");
        basketball.add("Tudengite öölaulupidu");
        basketball.add("Öölaulupeo järelpidu");
        basketball.add("Kastironimise eelvoor");
        basketball.add("Mälukas");


        expandableListDetail.put("Hitman333", cricket);
        expandableListDetail.put("Pannkoogihommik", football);
        expandableListDetail.put("Avarongkäik", basketball);

        expandableListDetail.put("Tudengite öölaulupidu", cricket);
        expandableListDetail.put("Öölaulupeo järelpidu", football);
        expandableListDetail.put("Kastironimise eelvoor", basketball);

        expandableListDetail.put("Mälukas", cricket);
        expandableListDetail.put("Pubiralli", football);
        expandableListDetail.put("Tantsumaraton", basketball);
        return expandableListDetail;
    }
}
