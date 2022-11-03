package org.springframework.samples.petclinic.recoveryroom;

import java.util.List;
import java.util.Optional;

public interface RecoveryRoomRepository {
    List<RecoveryRoom> findAll();
    Optional<RecoveryRoom> findById(int id);
    RecoveryRoom save(RecoveryRoom p);
    //List<RecoveryRoomType> findAllRecoveryRoomTypes();
    //RecoveryRoomType getRecoveryRoomType(String name);
}
