package davidhxxx.teach.jfreecharttuto.dataservice;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

public class LocalListServiceTest {

    @Test
    public void getNamesOfLists_returns_list_ordered_by_name() throws Exception {
	List<String> namesOfLists = LocalListService.getInstance().getNamesOfLists();
	List<String> expectedNamesOfLists = Arrays.asList("liste1", "liste2");
	ReflectionAssert.assertReflectionEquals(expectedNamesOfLists, namesOfLists, ReflectionComparatorMode.LENIENT_ORDER);
    }

}
