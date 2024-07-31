package com.virgo.rekomendasos;

import com.virgo.rekomendasos.controller.PlaceControllerTest;
import com.virgo.rekomendasos.controller.PostControllerTest;
import com.virgo.rekomendasos.repository.*;
import com.virgo.rekomendasos.service.GeoApiServiceTest;
import com.virgo.rekomendasos.service.PlaceServiceTest;
import com.virgo.rekomendasos.service.PostServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        PlaceControllerTest.class,
        PostControllerTest.class,
        PlaceRepositoryTest.class,
        PostRepositoryTest.class,
        UserRepositoryTest.class,
        VoucherRepositoryTest.class,
        VoucherTransactionRepositoryTest.class,
        GeoApiServiceTest.class,
        PlaceServiceTest.class,
        PostServiceTest.class
})
public class TestSuite {

}
