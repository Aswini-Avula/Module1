package com.hcl.fsc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.fsc.controllers.MasterTableController;
import com.hcl.fsc.entities.EmployeeDetails;
import com.hcl.fsc.entities.EmployeeEducationalDetails;
import com.hcl.fsc.entities.EmployeeOnboardingDetails;
import com.hcl.fsc.entities.EmployeeRecruitmentDetails;
import com.hcl.fsc.entities.Task;
import com.hcl.fsc.excel.vo.MoU;
import com.hcl.fsc.helpers.Constraints;
import com.hcl.fsc.helpers.EmployeeHelper;
import com.hcl.fsc.repositories.CategoryRepository;
import com.hcl.fsc.repositories.CollegeTieringRepository;
import com.hcl.fsc.repositories.EmployeeDetailsRepository;
import com.hcl.fsc.repositories.EmployeeEducationalDetailsRepository;
import com.hcl.fsc.repositories.EmployeeOnboardingDetailsRepository;
import com.hcl.fsc.repositories.EmployeeRecruitmentDetailsRepository;
import com.hcl.fsc.repositories.GenderRepository;
import com.hcl.fsc.repositories.GraduationSpecializationRepository;
import com.hcl.fsc.repositories.L1Repository;
import com.hcl.fsc.repositories.L2Repository;
import com.hcl.fsc.repositories.L3Repository;
import com.hcl.fsc.repositories.L4Repository;
import com.hcl.fsc.repositories.LocationRepository;
import com.hcl.fsc.repositories.MasterTablePossibleValuesRepository;
import com.hcl.fsc.repositories.OfferedBandRepository;
import com.hcl.fsc.repositories.OfferedDesignationRepository;
import com.hcl.fsc.repositories.OfferedSubBandRepository;
import com.hcl.fsc.repositories.RegionRepository;
import com.hcl.fsc.repositories.StateRepository;
import com.hcl.fsc.repositories.TaskRepository;
import com.hcl.fsc.repositories.UGOrPGRepository;
import com.hcl.fsc.repositories.UgDegreeRepository;
import com.hcl.fsc.response.ResponseList;

@Service
public class EmployeeMoUService {

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
	private StateRepository stateRepository;

	@Autowired
	private GraduationSpecializationRepository graduationSpecializationRepository;

	@Autowired
	private L2Repository l2Repository;

	@Autowired
	private L4Repository l4Repository;

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

	int rowNumber = 2;
	int duplicateRow = 0;

	public ResponseList employeeMoUListSave(MultipartFile file) {
		log.info("Validating and Saving data from MOU Sheet");
		List<String> errorsList = new ArrayList<>();
		ResponseList responseList = new ResponseList();
		List<String> duplicateEmailIdList = new ArrayList<>();
		List<String> duplicateRecords = new ArrayList<>();
		try {
			List<MoU> employeeMoUList = EmployeeHelper.convertExcelToListOfMoU(file.getInputStream());
			
//			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

			List<EmployeeDetails> employeeDetailsList = new ArrayList<>();
			List<EmployeeEducationalDetails> employeeEducationalDetailsList = new ArrayList<>();
			List<EmployeeOnboardingDetails> employeeOnboardingDetailsList = new ArrayList<>();
			List<EmployeeRecruitmentDetails> employeeRecruitmentDetailsList = new ArrayList<>();
			Set<String> genderSet = masterTablePossibleValuesRepository.findAll().stream()
					.filter(e -> e.getMasterTable().toLowerCase().equals("gender")).map(e -> e.getValue().toLowerCase())
					.collect(Collectors.toSet());

//			List<String> regionList = masterTablePossibleValuesRepository.findAll().stream()
//					.filter(e -> e.getMasterTable().toLowerCase().equals("region")).map(e -> e.getValue().toLowerCase())
//					.collect(Collectors.toList());

			Set<String> stateSet = masterTablePossibleValuesRepository.findAll().stream()
					.filter(e -> e.getMasterTable().toLowerCase().equals("state")).map(e -> e.getValue().toLowerCase())
					.collect(Collectors.toSet());

			Set<String> gradSpecializationSet = masterTablePossibleValuesRepository.findAll().stream()
					.filter(e -> e.getMasterTable().toLowerCase().equals("graduationspecialization"))
					.map(e -> e.getValue().toLowerCase()).collect(Collectors.toSet());

			Set<String> offeredDesignationSet = masterTablePossibleValuesRepository.findAll().stream()
					.filter(e -> e.getMasterTable().toLowerCase().equals("offereddesignation"))
					.map(e -> e.getValue().toLowerCase()).collect(Collectors.toSet());

			Set<String> offeredBandSet = masterTablePossibleValuesRepository.findAll().stream()
					.filter(e -> e.getMasterTable().toLowerCase().equals("band")).map(e -> e.getValue().toLowerCase())
					.collect(Collectors.toSet());
			Set<String> offeredSubBandSet = masterTablePossibleValuesRepository.findAll().stream()
					.filter(e -> e.getMasterTable().toLowerCase().equals("subband"))
					.map(e -> e.getValue().toLowerCase()).collect(Collectors.toSet());

			Set<String> l2Set = masterTablePossibleValuesRepository.findAll().stream()
					.filter(e -> e.getMasterTable().toLowerCase().equals("l2")).map(e -> e.getValue().toLowerCase())
					.collect(Collectors.toSet());

			Set<String> l4Set = masterTablePossibleValuesRepository.findAll().stream()
					.filter(e -> e.getMasterTable().toLowerCase().equals("l4")).map(e -> e.getValue().toLowerCase())
					.collect(Collectors.toSet());

			rowNumber = 2;
			duplicateRow = 0;
			employeeMoUList.stream().forEach(e -> {

				boolean flag = true;
				if (e.getEmail() != null) {

					if (employeeDetailsRepository.findById(e.getEmail()).equals(Optional.empty())) {
//						if (!Constraints.nameValidate(e.getName())) {
//
//							errorsList.add("name is not Correct at row" + rowNumber + " in mou excel sheet");
//							flag = true;
//						}
//						if (!Constraints.mobileNumberValidate(e.getContactNo())) {
//							errorsList.add("Contact number is not Correct at row" + rowNumber + " in mou excel sheet");
//							flag = true;
//						}
//						if (!Constraints.emailValidate(e.getEmail())) {
//
//							errorsList.add("Email Id is not Correct at row" + rowNumber + " in mou excel sheet");
//							flag = true;
//						}
						if (e.getGender() != null) {
							boolean gender = genderSet.contains(e.getGender().toLowerCase());
							if (!gender) {
								errorsList.add("values are null or improper in row " + rowNumber
										+ " in gender column of mou excel sheet");
								flag = false;
							}
						} else {
							errorsList.add("values are null or improper in row " + rowNumber
									+ " in gender column of mou excel sheet");
							flag = false;
						}

//						if (e.getRegion() != null) {
//							boolean region = regionList.contains(e.getRegion().toLowerCase());
//							if (!region) {
//								errorsList.add("values are null or improper in row " + rowNumber
//										+ " in region column of non-tier1 excel sheet");
//								flag = false;
//							}
//						} else {
//							errorsList.add("values are null or improper in row " + rowNumber
//									+ " in region column of non-tier1 excel sheet");
//							flag = false;
//						}

						if (e.getState() != null) {
							boolean state = stateSet.contains(e.getState().toLowerCase());
							if (!state) {
								errorsList.add("values are null or improper in row " + rowNumber
										+ " in state column of mou excel sheet");
								flag = false;
							}
						} else {
							errorsList.add("values are null or improper in row " + rowNumber
									+ " in state column of mou excel sheet");
							flag = false;
						}

						if (e.getGraduationSpecialization() != null) {
							boolean graduationSpecialization = gradSpecializationSet
									.contains(e.getGraduationSpecialization().toLowerCase());
							if (!graduationSpecialization) {
								errorsList.add("values are null or improper in row " + rowNumber
										+ " in graduation-specialization column of mou excel sheet");
								flag = false;
							}
						} else {
							errorsList.add("values are null or improper in row " + rowNumber
									+ " in graduation-specialization column of mou excel sheet");
							flag = false;
						}

						if (e.getOfferedDesignation() != null) {
							boolean offeredDesignation = offeredDesignationSet
									.contains(e.getOfferedDesignation().toLowerCase());
							if (!offeredDesignation) {
								errorsList.add("values are null or improper or improper in row " + rowNumber
										+ " in offered-designation column of mou excel sheet");
								flag = false;
							}
						} else {
							errorsList.add("values are null or improper or improper in row " + rowNumber
									+ " in offered-designation column of mou excel sheet");
							flag = false;
						}

						if (e.getOfferedBand() != null) {
							boolean offeredBand = offeredBandSet.contains(e.getOfferedBand().toLowerCase());
							if (!offeredBand) {
								errorsList.add("values are null or improper in row " + rowNumber
										+ " in offered-band column of mou excel sheet");
								flag = false;
							}
						} else {
							errorsList.add("values are null or improper in row " + rowNumber
									+ " in offered-band column of mou excel sheet");
							flag = false;
						}

						if (e.getOfferedSubBand() != null) {
							boolean offeredSubBand = offeredSubBandSet.contains(e.getOfferedSubBand().toLowerCase());
							if (!offeredSubBand) {
								errorsList.add("values are null or improper in row " + rowNumber
										+ " in offered-sub-band column of mou excel sheet");
								flag = false;
							}
						} else {
							errorsList.add("values are null or improper in row " + rowNumber
									+ " in offered-sub-band column of mou excel sheet");
							flag = false;
						}

						if (e.getL2() != null) {
							boolean l2 = l2Set.contains(e.getL2().toLowerCase());
							if (!l2) {
								errorsList.add("values are null or improper in row " + rowNumber
										+ " in L2 column of mou excel sheet");
								flag = false;
							}
						} else {
							errorsList.add("values are null or improper in row " + rowNumber
									+ " in L2 column of mou excel sheet");
							flag = false;
						}

						if (e.getL4() != null) {
							boolean l4 = l4Set.contains(e.getL4().toLowerCase());
							if (!l4) {
								errorsList.add("values are null or improper in row " + rowNumber
										+ " in L4 column of mou excel sheet");
								flag = false;
							}
						} else {
							errorsList.add("values are null or improper in row " + rowNumber
									+ " in L4 column of mou excel sheet");
							flag = false;
						}

						if (flag == true) {
							EmployeeDetails employeeDetails = new EmployeeDetails();
							employeeDetails = modelMapper.map(e, EmployeeDetails.class);
							employeeDetails.setGender(genderRepository
									.getByKey(masterTablePossibleValuesRepository.getByValueAndMasterTable(e.getGender(),"Gender").getKey()));
//							employeeDetails.setRegion(regionRepository
//									.getByKey(masterTablePossibleValuesRepository.getById(e.getRegion()).getKey()));
							employeeDetails.setState(stateRepository
									.getByKey(masterTablePossibleValuesRepository.getByValueAndMasterTable(e.getState(),"State").getKey()));
							employeeDetails.setSheetCode("MoU");
							employeeDetailsList.add(employeeDetails);

							EmployeeEducationalDetails employeeEducationalDetails = new EmployeeEducationalDetails();
							employeeEducationalDetails = modelMapper.map(e, EmployeeEducationalDetails.class);
							employeeEducationalDetails.setGraduationSpecialization(
									graduationSpecializationRepository.getByKey(masterTablePossibleValuesRepository
											.getByValueAndMasterTable(e.getGraduationSpecialization(),"GraduationSpecialization").getKey()));
							employeeEducationalDetails.setSheetCode("MoU");
							employeeEducationalDetailsList.add(employeeEducationalDetails);

							EmployeeOnboardingDetails employeeOnboardingDetails = new EmployeeOnboardingDetails();
							employeeOnboardingDetails = modelMapper.map(e, EmployeeOnboardingDetails.class);
							employeeOnboardingDetails.setOfferedDesignation(offeredDesignationRepository.getByKey(
									masterTablePossibleValuesRepository.getByValueAndMasterTable(e.getOfferedDesignation(),"OfferedDesignation").getKey()));
							employeeOnboardingDetails.setOfferedBand(offeredBandRepository.getByKey(
									masterTablePossibleValuesRepository.getByValueAndMasterTable(e.getOfferedBand(),"Band").getKey()));
							employeeOnboardingDetails.setOfferedSubBand(offeredSubBandRepository.getByKey(
									masterTablePossibleValuesRepository.getByValueAndMasterTable(e.getOfferedSubBand(),"SubBand").getKey()));
							employeeOnboardingDetails.setSheetCode("MoU");
							employeeOnboardingDetailsList.add(employeeOnboardingDetails);
							
							EmployeeRecruitmentDetails employeeRecruitmentDetails = new EmployeeRecruitmentDetails();
							employeeRecruitmentDetails = modelMapper.map(e, EmployeeRecruitmentDetails.class);
							employeeRecruitmentDetails.setL2(l2Repository
									.getByKey(masterTablePossibleValuesRepository.getByValueAndMasterTable(e.getL2(),"L2").getKey()));
							employeeRecruitmentDetails.setL4(l4Repository
									.getByKey(masterTablePossibleValuesRepository.getByValueAndMasterTable(e.getL4(),"L4").getKey()));
							employeeRecruitmentDetails.setSheetCode("MoU");
							employeeRecruitmentDetailsList.add(employeeRecruitmentDetails);

//							if (e.getSapId() != null  && e.getSapId() != 0) {
//								Task task = new Task();
//								if (e.getGraduationSpecialization() != null && e.getCollegeTiering() != null) {
//
//									if (e.getCollegeTiering().toUpperCase().equals("Tier-1")) {
//										if (e.getGraduationSpecialization().toUpperCase().equals("CSE")) {
//											task.setUserId(e.getSapId());
//											taskRepository.save(task);
//											employeeDetails.setCategory(categoryRepository.getById((long) 1));
//										} else {
//											task.setUserId(e.getSapId());
//											employeeDetails.setCategory(categoryRepository.getById((long) 2));
//										}
//									} else {
//										if (e.getGraduationSpecialization().toUpperCase().equals("CSE")) {
//											task.setUserId(e.getSapId());
//											taskRepository.save(task);
//											employeeDetails.setCategory(categoryRepository.getById((long) 3));
//
//										} else {
//											task.setUserId(e.getSapId());
//											taskRepository.save(task);
//											employeeDetails.setCategory(categoryRepository.getById((long) 4));
//										}
//									}
//								} else {
//									task.setUserId(e.getSapId());
//									taskRepository.save(task);
//									employeeDetails.setCategory(categoryRepository.getById((long) 5));
//								}
//							}

						}
					} else {
						
						duplicateRecords.add("duplicate entry at row no " + rowNumber
								+ " in mou excel sheet this email-id is already present in the database");
						duplicateEmailIdList.add(e.getEmail());
						duplicateRow++;
						flag = false;
					}
				} else {

					errorsList.add("email-id is null or improper in row no " + rowNumber + " in the mou excel sheet");
				}
				log.info(rowNumber + "row done--"+e.getEmail());
				rowNumber++;
			});

			employeeDetailsRepository.saveAll(employeeDetailsList);
			// System.out.println(employeeEducationalDetailsList);
			employeeEducationDetailsRepository.saveAll(employeeEducationalDetailsList);
			// System.out.println(employeeOnboardingDetailsList);
			employeeOnboardingDetailsRepository.saveAll(employeeOnboardingDetailsList);
			//
			employeeRecruitmentDetailsRepository.saveAll(employeeRecruitmentDetailsList);

			responseList.setTotal_No_Records(employeeMoUList.size());
			responseList.setSucessful_Records(employeeDetailsList.size());
		responseList.setFailed_Records(employeeMoUList.size() - duplicateRow-employeeDetailsList.size());
			responseList.setDuplicate_Records(duplicateRow);
			responseList.setDuplicate_Email_List(duplicateEmailIdList);

			Map<String, List<String>> duplicate_Records_List = new HashMap<>();
			duplicate_Records_List.put("MoU", duplicateRecords);
			responseList.setDuplicate_Records_List(duplicate_Records_List);

			Map<String, List<String>> failed_Records_List = new HashMap<>();
			failed_Records_List.put("MoU", errorsList);
			responseList.setFailed_Records_List(failed_Records_List);

		} catch (Exception e) {
			e.getStackTrace();
		}
		return responseList;

	}

}
