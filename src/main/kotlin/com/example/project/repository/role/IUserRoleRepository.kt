package com.example.project.repository.role

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface IUserRoleRepository : PagingAndSortingRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole>