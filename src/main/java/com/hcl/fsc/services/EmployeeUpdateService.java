package com.hcl.fsc.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.fsc.controllers.MasterTableController;
import com.hcl.fsc.entities.EmployeeAllDetails;
import com.hcl.fsc.entities.EmployeeDetails;
import com.hcl.fsc.entities.EmployeeEducationalDetails;
import com.hcl.fsc.entities.EmployeeOnboardingDetails;
import com.hcl.fsc.entities.EmployeeRecruitmentDetails;
import com.hcl.fsc.repositories.CategoryRepository;
import com.hcl.fsc.repositories.EmployeeDetailsRepository;
import com.hcl.fsc.repositories.EmployeeEducationalDetailsRepository;
import com.hcl.fsc.repositories.EmployeeOnboardingDetailsRepository;
import com.hcl.fsc.repositories.EmployeeRecruitmentDetailsRepository;
import com.hcl.fsc.repositories.GenderRepository;
import com.hcl.fsc.repositories.L2Repository;
import com.hcl.fsc.repositories.MasterTablePossibleValuesRepository;
import com.hcl.fsc.repositories.OfferedBandRepository;
import com.hcl.fsc.repositories.OfferedDesignationRepository;
import com.hcl.fsc.repositories.OfferedSubBandRepository;
import com.hcl.fsc.repositories.RegionRepository;
import com.hcl.fsc.repositories.StateRepository;
import com.hcl.fsc.repositories.TaskRepository;
import com.hcl.fsc.repositories.UgDegreeRepository;

@Service
public class EmployeeUpdateService {
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private EmployeeEducationalDetailsRepository employeeEducationDetailsRepository;

	@Autowired
	private EmployeeOnboardingDetailsRepository employeeOnboardingDetailsRepository;

	@Autowired
	private EmployeeRecruitmentDetailsRepository employeeRecruitmentDetailsRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private GenderRepository genderRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private UgDegreeRepository ugDegreeRepository;

	@Autowired
	private L2Repository l2Repository;

	@Autowired
	private OfferedDesignationRepository offeredDesignationRepository;

	@Autowired
	private OfferedBandRepository offeredBandRepository;

	@Autowired
	private OfferedSubBandRepository offeredSubBandRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private MasterTablePossibleValuesRepository masterTablePossibleValuesRepository;

	private static final Logger log = LoggerFactory.getLogger(MasterTableController.class);

	public String updatingEmployeeDetails(List<EmployeeAllDetails> listOfEmployeeAllDetails) {
		int res=0;
		for (int i = 0; i < listOfEmployeeAllDetails.size(); i++) {
			EmployeeDetails employeeDetails = this.employeeDetailsRepository
					.getById(listOfEmployeeAllDetails.get(i).getEmployeeDetails().getEmail());
			System.out.println(employeeDetails+"------");
			employeeDetails=modelMapper.map(listOfEmployeeAllDetails.get(i).getEmployeeDetails(), EmployeeDetails.class);
			System.out.println(employeeDetails);
//			this.employeeDetailsRepository.save(employeeDetails);
			
//			EmployeeEducationalDetails employeeEducationalDetails=this.employeeEducationDetailsRepository
//					.getById(listOfEmployeeAllDetails.get(i).getEmployeeEducationalDetails().getEmail());
//			employeeEducationalDetails=modelMapper.map(listOfEmployeeAllDetails.get(i), EmployeeEducationalDetails.class);
//			this.employeeEducationDetailsRepository.save(employeeEducationalDetails);
//			
//			EmployeeOnboardingDetails employeeOnboardingDetails=this.employeeOnboardingDetailsRepository
//					.getById(listOfEmployeeAllDetails.get(i).getEmployeeOnboardingDetails().getEmail());
//			employeeOnboardingDetails=modelMapper.map(listOfEmployeeAllDetails.get(i), EmployeeOnboardingDetails.class);
//			this.employeeOnboardingDetailsRepository.save(employeeOnboardingDetails);
//			
//			EmployeeRecruitmentDetails employeeRecruitmentDetails=this.employeeRecruitmentDetailsRepository
//					.getById(listOfEmployeeAllDetails.get(i).getEmployeeRecruitmentDetails().getEmail());
//			employeeRecruitmentDetails=modelMapper.map(listOfEmployeeAllDetails.get(i), EmployeeRecruitmentDetails.class);
//			this.employeeRecruitmentDetailsRepository.save(employeeRecruitmentDetails);
			res++;
		}
		if(res==listOfEmployeeAllDetails.size())
		return "All Details saved sucessfully!";
		else
			return res+"Details saved sucessfully only!";

	}
}
