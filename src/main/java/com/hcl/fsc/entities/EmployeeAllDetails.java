package com.hcl.fsc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAllDetails {
	
	private EmployeeDetails employeeDetails;
	private EmployeeEducationalDetails employeeEducationalDetails;
	private EmployeeOnboardingDetails employeeOnboardingDetails;
	private EmployeeRecruitmentDetails employeeRecruitmentDetails;

//	private String email;
//	private Long sapId;
//	private String name;
//	private String contactNo;
//	private String alternateContactNo;
//	private String address;
//	private String gender;
//	private String state;
//	private String region;
//	private Boolean ProjAssignedStatus = false;
//	private String category;
//	private String batch;
//	private String highSchoolPassingYear;
//	private String intermediatePercentage;
//	private String intermediatePassingYear;
//	private String graduationCollege;
//	private String graduationPassingYear;
//	private String universityRegistrationId;
//	private String postGraduationDegree;
//	private String postGraduationSpecialisation;
//	private String postGraduationPassingYear;
//	private String postGraduationPercentage;
//	private String pgCollegeName;
//	private String ugOrPg;
//	private String ugDegree;
//	private String graduationSpecialization;
//	private String onbaordingDetails;
//	private String onboardingStatus;
//	private String driveCollege;
//	private String driveDate;
//	private String offeredDesignation;
//	private String offeredBand;
//	private String offeredSubBand;
//	private String collegeTiering;
//	private String FPM_SPOC;
//	private String location;
//	private String internJoiningStatus;
//	private String internJoiningDate;
//	private String internSapID;
//	private String BR;
//	private String h1OrH2;
//	private String preOTPStatus;
//	private String tentativeJoiningMonth;
//	private String requisitionSource;
//	private String plannedDOJ;
//	private String month;
//	private String tentativeJoiningQTR;
//	private String tentativeDOJ;
//	private String joiningStatus;
//	private String tpPanel;
//	private String tpSap;
//	private String projectSkills;
//	private String lob;
//	private String team_Rdu;
//	private String hrPanel;
//	private String l1;
//	private String l2;
//	private String l3;
//	private String l4;
//	private String demandOwner;
//	private String demandOwnerSAP;
//	private String candidateMapping;
//	private String srMapping;
//	private String srStatus;
//	private String remappedProfile;
//	private String remappedLocation;
//	private String srNumber;
//	private String remarks;
//	private String sheetCode;
}
