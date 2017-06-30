package org.shanoir.ng.acquisitionequipment;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.shanoir.ng.center.CenterMapper;

/**
 * Mapper for acquisition equipments.
 * 
 * @author msimon
 *
 */
@Mapper(componentModel = "spring", uses = CenterMapper.class)
public interface AcquisitionEquipmentMapper {

	/**
	 * Map list of @AcquisitionEquipment to list of @AcquisitionEquipmentDTO.
	 * 
	 * @param acquisitionEquipments
	 *            list of acquisition equipments.
	 * @return list of acquisition equipments DTO.
	 */
	List<AcquisitionEquipmentDTO> acquisitionEquipmentsToAcquisitionEquipmentDTOs(
			List<AcquisitionEquipment> acquisitionEquipments);

	/**
	 * Map a @AcquisitionEquipment to a @AcquisitionEquipmentDTO.
	 * 
	 * @param acquisitionEquipment
	 *            acquisition equipment to map.
	 * @return acquisition equipment DTO.
	 */
	AcquisitionEquipmentDTO acquisitionEquipmentToAcquisitionEquipmentDTO(AcquisitionEquipment acquisitionEquipment);

	/**
	 * Map list of @AcquisitionEquipment to list of @AcquisitionEquipmentDTO.
	 * 
	 * @param acquisitionEquipments
	 *            list of acquisition equipments.
	 * @return list of acquisition equipments DTO.
	 */
	List<SimpleAcquisitionEquipmentDTO> acquisitionEquipmentsToSimpleAcquisitionEquipmentDTOs(
			List<AcquisitionEquipment> acquisitionEquipments);

	/**
	 * Map a @AcquisitionEquipment to a @AcquisitionEquipmentDTO.
	 * 
	 * @param acquisitionEquipment
	 *            acquisition equipment to map.
	 * @return acquisition equipment DTO.
	 */
	@Mappings({ @Mapping(source = "center.name", target = "centerName"),
			@Mapping(source = "manufacturerModel.name", target = "manufacturerModelName"),
			@Mapping(source = "manufacturerModel.manufacturer.name", target = "manufacturerName") })
	SimpleAcquisitionEquipmentDTO acquisitionEquipmentToSimpleAcquisitionEquipmentDTO(
			AcquisitionEquipment acquisitionEquipment);

}
