package com.example.demo.infra.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.share.enums.YesNo;

import jakarta.persistence.criteria.Predicate;

@Repository
public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long>, JpaSpecificationExecutor<GroupInfo> {

	List<GroupInfo> findByIdIn(List<Long> ids);

	List<GroupInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	List<GroupInfo> findByActiveFlag(YesNo activeFlag);

	List<GroupInfo> findAll(Specification<GroupInfo> specification);

	default Page<GroupInfo> findAllWithSpecification(String type, String name, String activeFlag, Integer numberOfRows,
			Integer pageNumber) {
		Specification<GroupInfo> specification = ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (StringUtils.isNotBlank(type)) {
				predicates.add(cb.equal(root.get("type"), type));
			}

			if (StringUtils.isNotBlank(name)) {
				Predicate preName = cb.like(root.get("name"), "%" + name + "%");
				Predicate preDesc = cb.like(root.get("description"), "%" + name + "%");
				Predicate combinedPredicate = cb.or(preName, preDesc);
				predicates.add(combinedPredicate);
			}

			if (StringUtils.isNotBlank(activeFlag)) {
				predicates.add(cb.equal(root.get("activeFlag"), activeFlag));
			} else {
				predicates.add(cb.equal(root.get("activeFlag"), "Y"));
			}

			Predicate[] predicateArray = new Predicate[predicates.size()];
			query.where(cb.and(predicates.toArray(predicateArray)));
			return query.getRestriction();
		});

		// 設置頁碼
		Pageable pageRequest = PageRequest.of(pageNumber, numberOfRows, Sort.unsorted());

		return findAll(specification, pageRequest);
	}
	
//	Page<GroupInfo> findAll(Specification<GroupInfo> specification, Pageable pageRequest);

	default List<GroupInfo> findAllWithSpecification(String str) {
		Specification<GroupInfo> specification = ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(str)) {
				Predicate predName = cb.like(root.get("name"), "%" + str + "%");
				Predicate predCode = cb.like(root.get("code"), "%" + str + "%");
				Predicate combinedPredicate = cb.or(predName, predCode);
				predicates.add(combinedPredicate);
			}

			predicates.add(cb.equal(root.get("activeFlag"), "Y"));

			Predicate[] predicateArray = new Predicate[predicates.size()];
			query.where(cb.and(predicates.toArray(predicateArray)));
			return query.getRestriction();
		});
		return findAll(specification);
	}
}
