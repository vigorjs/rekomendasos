package com.virgo.rekomendasos;

import com.virgo.rekomendasos.repository.PlaceRepositoryTest;
import com.virgo.rekomendasos.repository.PostRepositoryTest;
import com.virgo.rekomendasos.repository.UserRepositoryTest;
import com.virgo.rekomendasos.repository.VoucherRepositoryTest;
import com.virgo.rekomendasos.service.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        //Repository
        PlaceRepositoryTest.class,
//        PostRepositoryTest.class,
        UserRepositoryTest.class,
        VoucherRepositoryTest.class,
//        VoucherTransactionServiceTest.class,
        //Service
        AuthenticationServiceTest.class,
        CloudinaryServiceTest.class,
        LogTransactionServiceTest.class,
//        MidtransServiceTest.class,
        PlaceServiceTest.class,
//        PostServiceTest.class,
//        UserServiceTest.class,
//        VoucherServiceTest.class,
//        VoucherTransactionServiceTest.class
})
public class TestSuite {
}
