package com.surveasy.main.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surveasy.main.mapper.MainMapper;
import com.surveasy.main.model.MainSurveyObj;
import com.surveasy.survey.mapper.SurveyMapper;
import com.surveasy.survey.model.SurveyOption;
import com.surveasy.survey.model.SurveyPaper;
import com.surveasy.survey.model.SurveyRequire;

@Service
public class MainServiceImpl implements MainService {
	public static final Map<String, String> SUBJECT_MAP;

	static {
		Map<String, String> tempMap = new HashMap<>();
		tempMap.put("정치", "politics");
		tempMap.put("경제", "economy");
		tempMap.put("사회", "society");
		tempMap.put("문화", "culture");
		tempMap.put("과학", "science");
		tempMap.put("철학", "philosophy");
		SUBJECT_MAP = Collections.unmodifiableMap(tempMap);
	}

	@Autowired
	SurveyMapper surveyMapper;

	@Autowired
	MainMapper mainMapper;

	@Transactional
	@Override
	public List<MainSurveyObj> generateMainList() {
		List<MainSurveyObj> mainSurveyList = new ArrayList<>();
		List<SurveyPaper> surveyPaperList = mainMapper.getSurveyListByTime();

		for (int i = 0; i < surveyPaperList.size(); i++) {
			SurveyPaper surveyPaper = surveyPaperList.get(i);

			SurveyRequire surveyRequire = surveyMapper.getSurveyRequire(surveyPaper.getSurveyno());

			SurveyOption surveyOption = surveyMapper.getSurveyOption(surveyPaper.getSurveyno());

			MainSurveyObj mainSurvey = MainSurveyObj.builder().surveyno(surveyPaper.getSurveyno())
					.surveytitle(surveyPaper.getSurveytitle()).regidate(formatDateTime(surveyPaper.getRegidate()))
					.deadline(formatDateTime(surveyPaper.getDeadline())).participants(surveyPaper.getParticipants())
					.link(surveyPaper.getLink()).bookmark(surveyPaper.getBookmark()).subject(surveyRequire.getSubject())
					.target(surveyRequire.getTarget()).is_public_survey(surveyOption.is_public_survey()).build();

			mainSurveyList.add(mainSurvey);
		}
		return mainSurveyList;
	}

	@Override
	public List<MainSurveyObj> sortByRemainTime(List<MainSurveyObj> mainSurveyList) {
		RemainTimeComparator comp = new RemainTimeComparator();
		Collections.sort(mainSurveyList, comp);
		System.out.println("남은 시간순 정렬" + mainSurveyList);
		return mainSurveyList;
	}

	@Override
	public List<MainSurveyObj> sortByLatest(List<MainSurveyObj> mainSurveyList) {
		RegidateComparator comp = new RegidateComparator();
		Collections.sort(mainSurveyList, comp);
		System.out.println("최신순 정렬" + mainSurveyList);
		return mainSurveyList;
	}

	@Override
	public List<MainSurveyObj> sortByParticipants(List<MainSurveyObj> mainSurveyList) {
		ParticipantsComparator comp = new ParticipantsComparator();
		Collections.sort(mainSurveyList, comp);
		System.out.println("참여자순별 정렬" + mainSurveyList);
		return mainSurveyList;
	}

	@Override
	public List<MainSurveyObj> sortByBookmark(List<MainSurveyObj> mainSurveyList) {
		BookmarkComparator comp = new BookmarkComparator();
		Collections.sort(mainSurveyList, comp);
		System.out.println("즐겨찾기순 정렬" + mainSurveyList);
		return mainSurveyList;
	}

	@Override
	public List<MainSurveyObj> sortBySubject(List<MainSurveyObj> mainSurveyList, String subject) {

		String sortSubject = SUBJECT_MAP.get(subject);

		List<MainSurveyObj> sortedList = new ArrayList<>();
		Iterator<MainSurveyObj> iter = mainSurveyList.iterator();
		while (iter.hasNext()) {
			MainSurveyObj elem = iter.next();
			if (elem.getSubject().equals(sortSubject)) {
				sortedList.add(elem);
			}
		}
		System.out.println(subject + "별 정렬" + sortedList);
		return sortedList;
	}

	@Override
	public int getCurrentPage(List<MainSurveyObj> list, int pageNum) {
		if ((list.size() / 5) >= pageNum) {
			return pageNum;
		} else {
			return list.size() / 5;
		}
	}
	// 만약 2페이지가 없다면 0, 1페이지 왔다갔다해야하기때문에
	// 1, 2페이지 둘다 없다면 0페이지만 왔다갔다

	@Override
	public List<MainSurveyObj> listByPage(List<MainSurveyObj> list, int currentPage) {
		List<MainSurveyObj> mainSurveyList = new ArrayList<>();
		for (int i = 5 * currentPage; i < list.size(); i++) {
			mainSurveyList.add(list.get(i));
			if (mainSurveyList.size() == 5) {
				break;
			}
		}
		System.out.println("현재 페이지: " + currentPage);
		System.out.println("페이징: " + mainSurveyList);
		return mainSurveyList;
	}

	private String formatDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		return dateTime.format(formatter);
	}

}

class RemainTimeComparator implements Comparator<MainSurveyObj> {
	@Override
	public int compare(MainSurveyObj o1, MainSurveyObj o2) {
		if (o1 == null || o2 == null)
			return 0;
		return o1.getDeadline().compareTo(o2.getDeadline());
	}
}

class RegidateComparator implements Comparator<MainSurveyObj> {
	@Override
	public int compare(MainSurveyObj o1, MainSurveyObj o2) {
		if (o1 == null || o2 == null)
			return 0;
		return o2.getRegidate().compareTo(o1.getRegidate());
	}

}

class ParticipantsComparator implements Comparator<MainSurveyObj> {
	@Override
	public int compare(MainSurveyObj o1, MainSurveyObj o2) {
		if (o1 == null || o2 == null)
			return 0;
		return o2.getParticipants() - o1.getParticipants();
	}

}

class BookmarkComparator implements Comparator<MainSurveyObj> {
	@Override
	public int compare(MainSurveyObj o1, MainSurveyObj o2) {
		if (o1 == null || o2 == null)
			return 0;
		return o2.getBookmark() - o1.getBookmark();
	}

}
