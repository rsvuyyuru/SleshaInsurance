package com.slesha.planms.controller;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.Month;

import com.slesha.planms.dto.EnrollRequest;
import com.slesha.planms.entity.InsurancePlan;
import com.slesha.planms.service.InsurancePlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/planms")
public class InsurancePlanController {
    
    @Autowired
    InsurancePlanService service;

    @PostMapping()
    public ResponseEntity<String> addPlan(@RequestBody InsurancePlan plan){
        try{
            service.addPlan(plan);
            return new ResponseEntity<>("Plan Added",HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<InsurancePlan>> getPlans(){
        List<InsurancePlan> plans = service.getPlans();
        for(InsurancePlan plan: plans){
            if (LocalDate.now().getMonth() == Month.FEBRUARY) {
                //System.out.println(plan.getAveragePremium()+" intial");
                Double newAverage = plan.getAveragePremium()*0.9;
                //System.out.println("changes "+ newAverage);
                Double newMaximum = plan.getMaximumCoverage()*1.1;
                plan.setAveragePremium((int)Math.round(newAverage));
                plan.setMaximumCoverage((int)Math.round(newMaximum));
                //System.out.println(plan.getAveragePremium());
            }
        }
        return new ResponseEntity<>(plans,HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InsurancePlan> getPlan(@PathVariable Integer id){
        Optional<InsurancePlan> data=service.getPlan(id);
        if(data.isPresent()){
            return new ResponseEntity<>(data.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     
    }
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollInPlan(@RequestBody EnrollRequest req){

        System.out.println(req);
        service.enroll(req);
        return new ResponseEntity<>("Enrolled Successfully",HttpStatus.CREATED);
            
    }
    
}
