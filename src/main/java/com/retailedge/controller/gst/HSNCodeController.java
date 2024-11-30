package com.retailedge.controller.gst;

import com.retailedge.entity.gst.HSNCode;
import com.retailedge.dto.gst.HSNCodeDto;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.gst.HSNCodeService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hsn-code")
public class HSNCodeController {

    @Autowired
    private HSNCodeService hsnCodeService;

    @GetMapping
    public List<HSNCode> list(){
        return hsnCodeService.list();
    }

    @PostMapping
    public HSNCode add(@RequestBody HSNCodeDto hsnCodeDto){
        return hsnCodeService.add(hsnCodeDto);
    }

    @PutMapping("/{hsnCodeId}")
    public HSNCode update(@PathVariable("hsnCodeId") Long hsnCodeId, @RequestBody HSNCodeDto hsnCodeDto){
        return hsnCodeService.update(hsnCodeId, hsnCodeDto);
    }

    @DeleteMapping("/{hsnCodeId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("hsnCodeId") Long hsnCodeId) throws Exception {
       return hsnCodeService.delete(hsnCodeId);
    }

}
