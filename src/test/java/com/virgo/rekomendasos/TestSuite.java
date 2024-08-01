package com.virgo.rekomendasos;

import com.virgo.rekomendasos.controller.PlaceControllerTest;
import com.virgo.rekomendasos.controller.PostControllerTest;
import com.virgo.rekomendasos.repository.*;
import com.virgo.rekomendasos.service.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        //Controller
        PlaceControllerTest.class,
        PostControllerTest.class,

        //Repository
        PlaceRepositoryTest.class,
        PostRepositoryTest.class,
        UserRepositoryTest.class,
        VoucherRepositoryTest.class,
        VoucherTransactionRepositoryTest.class,

        //Service
        AuthenticationServiceTest.class,
        CloudinaryServiceTest.class,
        LogTransactionServiceTest.class,
        VoucherServiceTest.class,
        VoucherTransactionServiceTest.class,
        UserServiceTest.class,
        GeoApiServiceTest.class,
        PlaceServiceTest.class,
        PostServiceTest.class
})
public class TestSuite {

}

