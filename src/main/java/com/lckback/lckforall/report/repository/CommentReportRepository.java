package com.lckback.lckforall.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lckback.lckforall.report.model.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
}
