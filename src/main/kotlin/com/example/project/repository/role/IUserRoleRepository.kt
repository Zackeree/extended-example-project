package com.example.project.repository.role

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
@Qualifier(value = "userRoleRepo")
interface IUserRoleRepository : PagingAndSortingRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole>