package org.springframework.samples.petclinic.recoveryroom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test10 {
    @Autowired
    RecoveryRoomService rs;


    @Test
    public void test10(){
        testSaveRecoveryRoomSuccessfull();
        testSaveUnfeasibleRecoveryRoom();
        testAnnotations();
    }

    @Transactional
    public void testSaveRecoveryRoomSuccessfull()
    {
        RecoveryRoomType roomType=rs.getRecoveryRoomType("room"); 
        RecoveryRoom rr = new RecoveryRoom();
        rr.setRoomType(roomType);
        rr.setName("test");
        rr.setSecure(true);
        rr.setSize(5);
        try {
            rs.save(rr);
        } catch (DuplicatedRoomNameException e) {
            fail("The excepcion should not be thrown, the RecoveryRoom is feasible!");
        }
    }

    @Transactional
    public void testSaveUnfeasibleRecoveryRoom()
    {
        RecoveryRoomType roomType=rs.getRecoveryRoomType("box"); 
        RecoveryRoom rr = new RecoveryRoom();
        rr.setRoomType(roomType);
        rr.setName("Medium box"); //already existing!
        rr.setSecure(false);
        rr.setSize(2);
        // Thus, the save should throw an exception:
        assertThrows(DuplicatedRoomNameException.class,
            ()-> rs.save(rr));
    }

    public void testAnnotations() {
        Method save=null;
        try {
            save = RecoveryRoomService.class.getDeclaredMethod("save", RecoveryRoom.class);
        } catch (NoSuchMethodException e) {
           fail("There is no method save in RecoveryRoomService");
        } catch (SecurityException e) {
            fail("There is no accessible method save in RecoveryRoomService");
        }
        Transactional transactionalAnnotation=save.getAnnotation(Transactional.class);
        assertNotNull(transactionalAnnotation,"The method save is not annotated as transactional");
        List<Class<? extends Throwable>> exceptionsWithRollbackFor=Arrays.asList(transactionalAnnotation.rollbackFor());
        assertTrue(exceptionsWithRollbackFor.contains(DuplicatedRoomNameException.class),"You should configure rollback for DuplicatedRoomNameException");
    }

}
