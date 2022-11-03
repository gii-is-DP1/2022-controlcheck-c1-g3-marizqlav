package org.springframework.samples.petclinic.recoveryroom;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class RecoveryRoomTypeFormatter implements Formatter<RecoveryRoomType>{

	private RecoveryRoomService recoveryRoomService;
	
	@Autowired
	public RecoveryRoomTypeFormatter(RecoveryRoomService recoveryRoomService) {
		this.recoveryRoomService=recoveryRoomService;
	}
	

    @Override
    public String print(RecoveryRoomType object, Locale locale) {
        return object.getName();
    }

    @Override
    public RecoveryRoomType parse(String text, Locale locale) throws ParseException {
    	List<RecoveryRoomType> recoveryRoomTypes=recoveryRoomService.getAllRecoveryRoomTypes();
    	for (RecoveryRoomType r:recoveryRoomTypes) {
			if (r.getName().equals(text)) {
				return r;
			}
		} throw new ParseException("ProductType not found"+ text, 0);
    }

    
}
