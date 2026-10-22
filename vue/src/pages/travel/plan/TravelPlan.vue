<template>
	<div class="min-h-screen bg-white">
		<!-- 상단 고정 영역 -->
		<div
			class="fixed top-16 z-20 w-[1278px] bg-white border-b border-gray-200"
		>
			<!-- 여행 정보 -->
			<div class="h-[100px] px-6 flex items-center justify-between">
				<div>
					<div class="flex items-center gap-2">
						<h1 class="text-3xl font-bold">
							{{
								planInfo?.title ??
								"이번 여행의 제목을 입력해보세요."
							}}
						</h1>

						<button class="text-gray-400 hover:text-teal-500">
							<font-awesome-icon :icon="['fas', 'pen']" />
						</button>
					</div>

					<p class="mt-2 text-sm text-gray-500">
						여행 동료와 함께 여행 계획을 작성해보세요
					</p>
				</div>

				<div class="flex items-center gap-5">
					<!-- 참여자 -->
					<div class="flex items-center">
						<img
							v-for="(user, index) in connectedUsers.slice(0, 3)"
							:key="user.userId"
							:src="getImageSrc(user.profilePath)"
							class="w-9 h-9 rounded-full border-2 border-white"
							:class="index > 0 ? '-ml-2' : ''"
						/>

						<div
							v-if="connectedUsers.length > 5"
							class="w-9 h-9 rounded-full bg-gray-100 border-2 border-white -ml-2 flex items-center justify-center text-gray-500"
						>
							+{{ connectedUsers.length - 5 }}
						</div>
					</div>

					<!-- 초대 -->
					<button
						@click="isInviteModalOpen = true"
						class="bg-teal-500 hover:bg-teal-600 text-white px-5 h-[42px] rounded-lg cursor-pointer"
					>
						<font-awesome-icon
							:icon="['fas', 'user-plus']"
							class="mr-1"
						/>
						초대하기
					</button>
				</div>
			</div>

			<!-- 탭 -->
			<div class="h-[58px] px-6 flex items-end gap-10">
				<button
					class="h-full px-3 border-b-[3px] border-teal-500 text-teal-500 font-semibold cursor-pointer"
				>
					<font-awesome-icon
						:icon="['far', 'calendar']"
						class="mr-2"
					/>
					일정
				</button>

				<button
					class="h-full px-3 border-b-[3px] border-transparent text-gray-500 hover:text-teal-500 cursor-pointer"
				>
					<font-awesome-icon
						:icon="['far', 'note-sticky']"
						class="mr-2"
					/>
					전체 메모
				</button>
			</div>
		</div>

		<!-- 본문 -->
		<div class="pt-[185px] pb-20 px-6">
			<div class="max-w-[1050px] mx-auto">
				<!-- 여행 개요 + 경비 -->
				<div class="grid grid-cols-[270px_1fr] gap-6">
					<!-- 좌측 여행 개요 -->
					<div
						class="border border-gray-200 rounded-xl p-5 h-fit bg-white shadow-sm"
					>
						<div class="font-bold text-lg mb-5">여행 개요</div>

						<div
							class="flex items-center gap-3 text-sm text-gray-600 mb-4"
						>
							<font-awesome-icon
								:icon="['far', 'calendar']"
								class="w-4"
							/>
							<span
								>{{
									formatToFullDateWithDay(planInfo?.startAt)
								}}
								~
								{{ formatToFullDateWithDay(planInfo?.endAt) }}
							</span>
						</div>

						<div
							class="flex items-center gap-3 text-sm text-gray-600 mb-4"
						>
							<font-awesome-icon
								:icon="['fas', 'location-dot']"
								class="w-4"
							/>
							<span>{{
								planInfo?.place ?? "여행지를 입력해주세요."
							}}</span>
						</div>

						<div
							class="flex items-center gap-3 text-sm text-gray-600 mb-6"
						>
							<font-awesome-icon
								:icon="['fas', 'users']"
								class="w-4"
							/>
							<span
								>참가자 {{ planInfo?.memberCount ?? 1 }}명</span
							>
						</div>

						<div
							class="border-t border-gray-100 pt-5 text-sm leading-6 text-gray-500"
						>
							푸른 바다와 맛있는 음식을 즐기는 힐링 여행! 좋은
							사람들과 잊지 못할 추억을 만들어보세요.
						</div>

						<button
							@click="isTravelUpdateModalOpen = true"
							class="mt-6 w-full h-[42px] border border-gray-300 rounded-lg text-sm hover:bg-gray-50 cursor-pointer"
						>
							<font-awesome-icon
								:icon="['fas', 'pen']"
								class="mr-2"
							/>
							여행 정보 수정
						</button>
					</div>

					<!-- 우측 -->
					<div>
						<!-- 총 예상 경비 -->
						<div
							class="border border-gray-200 rounded-xl p-6 bg-white shadow-sm"
						>
							<div class="flex justify-between items-start">
								<div>
									<div
										class="text-sm font-semibold text-gray-600"
									>
										1인 기준 예상 경비
									</div>

									<div
										class="mt-2 text-3xl font-bold text-teal-500"
									>
										{{ totalCost() }}원
									</div>
								</div>
							</div>

							<div
								class="flex w-full h-[20px] mt-3 rounded-full overflow-hidden"
							>
								<div
									v-for="item in costChartList"
									:key="item.label"
									v-tooltip="
										`${item.label}: ${item.cost.toLocaleString()}원`
									"
									:style="{ width: `${item.percent}%` }"
									:class="item.bgClass"
									class="h-full"
								/>
							</div>

							<!-- 카테고리 -->
							<div class="grid grid-cols-5 gap-3 mt-5">
								<div class="rounded-lg bg-blue-50 p-4">
									<div class="font-semibold text-sm">
										<font-awesome-icon
											:icon="['fas', 'plane']"
										/>
										교통
									</div>
									<div class="text-xs text-gray-500 mt-2">
										{{
											costChartList[0]?.cost.toLocaleString()
										}}원
									</div>
								</div>

								<div class="rounded-lg bg-purple-50 p-4">
									<div class="font-semibold text-sm">
										<font-awesome-icon
											:icon="['fas', 'bed']"
										/>
										숙박
									</div>
									<div class="text-xs text-gray-500 mt-2">
										{{
											costChartList[1]?.cost.toLocaleString()
										}}원
									</div>
								</div>

								<div class="rounded-lg bg-orange-50 p-4">
									<div class="font-semibold text-sm">
										<font-awesome-icon
											:icon="['fas', 'utensils']"
										/>
										식비
									</div>
									<div class="text-xs text-gray-500 mt-2">
										{{
											costChartList[2]?.cost.toLocaleString()
										}}원
									</div>
								</div>

								<div class="rounded-lg bg-teal-50 p-4">
									<div class="font-semibold text-sm">
										<font-awesome-icon
											:icon="['fas', 'camera']"
										/>
										관광
									</div>
									<div class="text-xs text-gray-500 mt-2">
										{{
											costChartList[3]?.cost.toLocaleString()
										}}원
									</div>
								</div>

								<div class="rounded-lg bg-gray-50 p-4">
									<div class="font-semibold text-sm">
										••• 기타
									</div>
									<div class="text-xs text-gray-500 mt-2">
										{{
											costChartList[4]?.cost.toLocaleString()
										}}원
									</div>
								</div>
							</div>
						</div>

						<div ref="planContainer" class="relative">
							<div
								v-for="item in indicatorPositions"
								:key="item.userId"
								v-show="
									indicatorPositions[item.userId] &&
									indicatorPositions[item.userId]?.visible
								"
								class="editing-indicator"
								:style="{
									top: `${
										indicatorPositions[item.userId]?.top ??
										0
									}px`,
									left: `${
										indicatorPositions[item.userId]?.left ??
										0
									}px`,
								}"
							>
								{{ item.nickName }}님이
								{{ item.isEditing ? "수정" : "조회" }}중...
							</div>

							<draggable
								v-model="dayInfoList"
								item-key="id"
								@end="updateDayOrder"
								animation="200"
								class="mt-5"
							>
								<template #item="{ element: plan, index }">
									<div
										@click="
											sendMyIndicator(
												$event,
												plan.id,
												null,
											)
										"
										:data-plan-id="plan.id"
										class="mb-3"
									>
										<div
											v-if="
												dayPlanModiOpenIndex !== index
											"
											class="h-[72px] w-[815px] border border-gray-200 rounded-xl px-5 flex items-center justify-between bg-white shadow-sm cursor-pointer"
											:class="{
												'border-yellow-400':
													isEditingSchedule(
														plan.id,
														null,
													),
											}"
										>
											<div
												class="flex items-center gap-4"
											>
												<font-awesome-icon
													@click.stop="
														refreshIndicatorPosition(
															plan.id,
														)
													"
													:icon="[
														'fas',
														'chevron-down',
													]"
													class="text-gray-400 text-xs"
												/>

												<div>
													<div
														class="font-bold text-xl"
													>
														{{ index + 1 }}일차 -
														{{
															plan.title != ""
																? plan.title
																: "제목을 입력해주세요."
														}}
													</div>

													<div
														class="mt-1 text-sm text-gray-500"
													>
														{{
															plan.date
																? formatToFullDateWithDay(
																		plan.date,
																	)
																: "날짜를 선택해주세요."
														}}
													</div>
												</div>
											</div>

											<div
												class="flex items-center gap-6"
											>
												<div
													class="bg-teal-50 text-teal-600 rounded-full px-3 py-1 text-sm font-semibold"
												>
													총
													{{
														getDayCost(
															plan.schedules,
														)
													}}원
												</div>

												<div class="relative">
													<div
														class="hover:bg-gray-100 cursor-pointer rounded-md"
														@click.stop="
															dayPlanMenuOpenIndex =
																index
														"
													>
														<font-awesome-icon
															:icon="[
																'fas',
																'ellipsis-vertical',
															]"
															class="text-gray-400"
														/>
													</div>
													<div
														v-if="
															dayPlanMenuOpenIndex ===
															index
														"
														class="bg-white rounded-lg w-[50px] border-1 border-gray-200 absolute z-999"
													>
														<div
															@click.stop="
																((dayPlanModiOpenIndex =
																	index),
																(dayPlanMenuOpenIndex =
																	-1))
															"
															class="text-center py-1 cursor-pointer hover:bg-gray-100 mt-1"
														>
															수정
														</div>
														<div
															@click.stop="
																deleteDayPlan(
																	plan.id,
																	index,
																)
															"
															class="text-center py-1 cursor-pointer hover:bg-gray-100 mb-1"
														>
															삭제
														</div>
													</div>
												</div>
											</div>
										</div>

										<!-- 일자 수정 -->
										<div
											v-if="dayPlanModiOpenIndex == index"
											class="border border-teal-200 bg-teal-50 rounded-xl px-5 shadow-sm cursor-pointer py-3"
										>
											<div>
												<div>
													<div
														class="font-bold text-xl"
													>
														<font-awesome-icon
															@click.stop="
																toggleDayPlanMenu(
																	index,
																)
															"
															:icon="[
																'fas',
																'chevron-down',
															]"
															class="text-gray-400 text-xs"
														/>
														{{ index + 1 }}일차 -
														{{
															plan.title === ""
																? "제목을 입력하세요"
																: plan.title
														}}
														<span
															class="text-teal-400"
															>(수정 중)</span
														>
													</div>
													<div
														class="pl-5 mt-3 flex flex-row justify-between w-full"
													>
														<div class="w-[48%]">
															<div
																class="font-bold required"
															>
																일차 제목
															</div>
															<div
																class="border border-gray-300 rounded-lg bg-white w-full h-[30px] relative hover-green"
															>
																<input
																	:value="
																		plan.title
																	"
																	@input="
																		planTitleInput(
																			$event,
																			index,
																			plan.id,
																		)
																	"
																	maxlength="30"
																	type="text"
																	placeholder="제목을 입력하세요."
																	class="pl-3 pr-13 w-full custom-input h-full"
																	data-is-mod="true"
																	@click.stop="
																		sendMyIndicator(
																			$event,
																			plan.id,
																			null,
																		)
																	"
																/>
																<span
																	class="text-gray-400 absolute right-2"
																	>{{
																		plan
																			.title
																			.length
																	}}/30</span
																>
															</div>
														</div>
														<div class="w-[48%]">
															<div class="w-full">
																<span
																	class="font-bold required"
																>
																	날짜
																</span>
																<div
																	class="hover-green relative border-gray rounded-md p-1 pl-6 h-[30px] bg-white"
																>
																	<flat-pickr
																		v-model="
																			plan.date
																		"
																		@on-change="
																			planDateInput(
																				index,
																				plan.id,
																			)
																		"
																		:config="
																			dayConfig
																		"
																		class="form-control cursor-pointer w-full"
																		placeholder="날짜를 선택하세요"
																		name="date"
																		autocomplete="off"
																		data-is-mod="true"
																		@click.stop="
																			sendMyIndicator(
																				$event,
																				plan.id,
																				null,
																			)
																		"
																	/>
																	<span
																		class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 top-1/2 dark:text-gray-400"
																	>
																		<i
																			class="bi bi-calendar4"
																		></i>
																	</span>
																</div>
															</div>
														</div>
													</div>
													<div
														class="mt-2 flex flex-row justify-end"
													>
														<button
															@click.stop="
																((dayPlanModiOpenIndex =
																	-1),
																sendMyIndicator(
																	$event,
																	plan.id,
																	null,
																))
															"
															class="btn bg-teal-500 h-[30px] w-[70px] text-white"
														>
															확인
														</button>
													</div>
												</div>
											</div>
										</div>
										<!-- 스케줄 -->
										<draggable
											v-if="toggleDayPlanRecord[plan.id]"
											v-model="plan.schedules"
											item-key="id"
											@end="
												updateScheduleOrder(
													plan.id,
													plan.schedules,
												)
											"
											animation="200"
										>
											<template
												#item="{
													element: schedule,
													index: scheduleIndex,
												}"
											>
												<div
													class="relative pl-[115px] mt-4 cursor-pointer"
													:data-schedule-id="
														schedule.id
													"
													@click.stop="
														sendMyIndicator(
															$event,
															plan.id,
															schedule.id,
														)
													"
												>
													<div
														class="absolute left-[54px] top-0 bottom-0 w-px bg-gray-200"
													></div>

													<div
														class="absolute left-0 top-3 w-[90px]"
													>
														<div
															class="flex items-center gap-2"
														>
															<div
																class="w-3 h-3 rounded-full bg-teal-500"
															></div>
															<span
																class="font-semibold"
															>
																{{
																	schedule.startTime
																}}</span
															>
														</div>

														<div
															class="ml-5 mt-1 text-sm text-gray-400"
														>
															{{
																schedule.endTime
															}}
														</div>
													</div>

													<div
														v-if="
															schedulePlanModiOpenIndex.dayIndex ===
																index &&
															schedulePlanModiOpenIndex.scheduleIndex ===
																scheduleIndex
														"
														class="border-2 border-teal-400 rounded-xl px-5 py-4 w-[700px]"
													>
														<div class="w-full">
															<div
																class="flex flex-row"
															>
																<div
																	class="w-full"
																>
																	<span
																		>시작
																		시간</span
																	>
																	<div
																		class="hover-green relative border-gray rounded-md pl-3 h-[30px] w-[98%]"
																	>
																		<flat-pickr
																			v-model="
																				schedule.startTime
																			"
																			@on-change="
																				(
																					selectedDates,
																					dateStr,
																				) => {
																					updateScheduleTime(
																						dateStr,
																						plan.id,
																						schedule.id,
																						'startTime',
																					);
																				}
																			"
																			:config="
																				scheduleConfig
																			"
																			class="form-control cursor-pointer w-full"
																			placeholder="날짜를 선택하세요"
																			name="date"
																			autocomplete="off"
																			data-is-mod="true"
																			@click.stop="
																				sendMyIndicator(
																					$event,
																					plan.id,
																					schedule.id,
																				)
																			"
																		/>
																		<span
																			class="absolute text-gray-500 -translate-y-1/2 pointer-events-none right-1 top-1/2 dark:text-gray-400"
																		>
																			<font-awesome-icon
																				:icon="[
																					'far',
																					'clock',
																				]"
																				class="text-gray-400"
																			/>
																		</span>
																	</div>
																</div>
																<div
																	class="w-full"
																>
																	<span
																		>종료
																		시간</span
																	>
																	<div
																		class="hover-green relative border-gray rounded-md pl-3 h-[30px] w-[98%]"
																	>
																		<flat-pickr
																			v-model="
																				schedule.endTime
																			"
																			@on-change="
																				(
																					selectedDates,
																					dateStr,
																				) => {
																					updateScheduleTime(
																						dateStr,
																						plan.id,
																						schedule.id,
																						'endTime',
																					);
																				}
																			"
																			:config="
																				scheduleConfig
																			"
																			class="form-control cursor-pointer w-full"
																			placeholder="날짜를 선택하세요"
																			name="date"
																			autocomplete="off"
																			data-is-mod="true"
																			@click.stop="
																				sendMyIndicator(
																					$event,
																					plan.id,
																					schedule.id,
																				)
																			"
																		/>
																		<span
																			class="absolute text-gray-500 -translate-y-1/2 pointer-events-none right-1 top-1/2 dark:text-gray-400"
																		>
																			<font-awesome-icon
																				:icon="[
																					'far',
																					'clock',
																				]"
																				class="text-gray-400"
																			/>
																		</span>
																	</div>
																</div>
															</div>
															<div class="mt-2">
																<span
																	>일정
																	제목</span
																>
																<div
																	class="border border-gray-300 rounded-lg w-full h-[30px] relative hover-green"
																>
																	<input
																		type="text"
																		v-model="
																			schedule.title
																		"
																		@input="
																			updateSchedule(
																				$event,
																				plan.id,
																				schedule.id,
																				'title',
																			)
																		"
																		class="pl-3 pr-13 w-full custom-input h-full"
																		data-is-mod="true"
																		@click.stop="
																			sendMyIndicator(
																				$event,
																				plan.id,
																				schedule.id,
																			)
																		"
																	/>
																</div>
															</div>
															<div class="mt-2">
																<span
																	>장소</span
																>
																<div
																	class="border border-gray-300 rounded-lg w-full h-[30px] relative hover-green"
																>
																	<input
																		type="text"
																		v-model="
																			schedule
																				.place
																				.name
																		"
																		@input="
																			updateSchedule(
																				$event,
																				plan.id,
																				schedule.id,
																				'name',
																			)
																		"
																		class="pl-3 pr-13 w-full custom-input h-full"
																		data-is-mod="true"
																		@click.stop="
																			sendMyIndicator(
																				$event,
																				plan.id,
																				schedule.id,
																			)
																		"
																	/>
																</div>
															</div>
															<div
																class="mt-2 flex w-full justify-between"
															>
																<!-- 비용 계산기 -->
																<div
																	class="w-[49%]"
																>
																	<!-- 라벨 + 연산 버튼 -->
																	<div
																		class="mb-1 flex items-center justify-between"
																	>
																		<span
																			class="text-sm"
																		>
																			비용
																			계산기
																		</span>

																		<div
																			class="flex gap-1"
																		>
																			<button
																				@click.stop="
																					(calculateCost(
																						schedule,
																						'plus',
																						plan.id,
																					),
																					sendMyIndicator(
																						$event,
																						plan.id,
																						schedule.id,
																					))
																				"
																				data-is-mod="true"
																				type="button"
																				class="w-[24px] h-[24px] border border-gray-300 text-gray-500 rounded text-sm font-semibold hover:border-teal-500 cursor-pointer"
																			>
																				+
																			</button>

																			<button
																				@click="
																					(calculateCost(
																						schedule,
																						'min',
																						plan.id,
																					),
																					sendMyIndicator(
																						$event,
																						plan.id,
																						schedule.id,
																					))
																				"
																				type="button"
																				class="w-[24px] h-[24px] border border-gray-300 text-gray-500 rounded text-sm font-semibold hover:border-teal-500 hover:text-teal-500 cursor-pointer"
																			>
																				−
																			</button>
																		</div>
																	</div>

																	<!-- 계산할 금액 -->
																	<div
																		class="border border-gray-300 rounded-lg w-full h-[30px] relative hover-green flex items-center"
																	>
																		<span
																			class="absolute left-2 text-sm text-gray-400"
																		>
																			₩
																		</span>

																		<input
																			v-model="
																				calCost
																			"
																			@keyup.enter="
																				calculateCost(
																					schedule,
																					'plus',
																					plan.id,
																				)
																			"
																			data-is-mod="true"
																			@click.stop="
																				sendMyIndicator(
																					$event,
																					plan.id,
																					schedule.id,
																				)
																			"
																			type="number"
																			placeholder="계산할 금액"
																			class="pl-6 pr-3 w-full h-full custom-input text-sm"
																		/>
																	</div>
																</div>

																<!-- 예상 비용 -->
																<div
																	class="w-[49%]"
																>
																	<div
																		class="mb-2 flex items-center justify-between"
																	>
																		<span
																			class="text-sm"
																		>
																			예상
																			비용(원)
																		</span>
																	</div>

																	<div
																		class="border border-teal-200 bg-teal-50 rounded-lg w-full h-[30px] relative flex items-center"
																	>
																		<span
																			class="absolute left-2 text-sm text-teal-600"
																		>
																			₩
																		</span>

																		<input
																			type="number"
																			v-model="
																				schedule.cost
																			"
																			@input="
																				updateSchedule(
																					$event,
																					plan.id,
																					schedule.id,
																					'cost',
																				)
																			"
																			data-is-mod="true"
																			@click.stop="
																				sendMyIndicator(
																					$event,
																					plan.id,
																					schedule.id,
																				)
																			"
																			class="pl-6 pr-3 w-full h-full custom-input text-sm font-semibold text-teal-600"
																		/>
																	</div>
																</div>
															</div>
															<div class="mt-2">
																<span
																	>메모</span
																>
																<div
																	class="border border-gray-300 rounded-lg w-full relative hover-green"
																>
																	<textarea
																		ref="textareaRef"
																		type="text"
																		v-model="
																			schedule.memo
																		"
																		@input="
																			updateSchedule(
																				$event,
																				plan.id,
																				schedule.id,
																				'memo',
																			)
																		"
																		data-is-mod="true"
																		@click.stop="
																			sendMyIndicator(
																				$event,
																				plan.id,
																				schedule.id,
																			)
																		"
																		placeholder="대한항공 KE1253편"
																		class="pl-3 pr-13 w-full custom-input h-full"
																	/>
																</div>
															</div>
															<div class="mt-2">
																<span
																	>카테고리</span
																>
																<div
																	class="flex flex-row"
																>
																	<div
																		v-for="item in scheduleCategories"
																		:key="
																			item.type
																		"
																		@click.stop="
																			(updateScheduleCategory(
																				item.type,
																				plan.id,
																				schedule.id,
																			),
																			(schedule.type =
																				item.type),
																			sendMyIndicator(
																				$event,
																				plan.id,
																				schedule.id,
																			))
																		"
																		data-is-mod="true"
																		class="mr-1 inline-flex items-center px-3 py-1.5 rounded-full text-xs font-medium border transition-all cursor-pointer whitespace-nowrap bg-gray-50 text-gray-500 border-gray-200 hover:bg-gray-100"
																		:class="
																			schedule.type ===
																			item.type
																				? item.chipClass
																				: ''
																		"
																	>
																		<font-awesome-icon
																			:icon="
																				item.icon
																			"
																			class="mr-1.5"
																		/>
																		{{
																			item.label
																		}}
																	</div>
																</div>
															</div>
															<div
																class="mt-2 flex flex-row justify-end"
															>
																<button
																	@click="
																		deleteSchedule(
																			plan.id,
																			index,
																			schedule.id,
																			scheduleIndex,
																		)
																	"
																	class="btn bg-red-500 h-[30px] w-[70px] text-white mr-2"
																>
																	삭제
																</button>
																<button
																	@click="
																		((schedulePlanModiOpenIndex.dayIndex =
																			-1),
																		(schedulePlanModiOpenIndex.scheduleIndex =
																			-1),
																		sendMyIndicator(
																			$event,
																			plan.id,
																			schedule.id,
																		))
																	"
																	class="btn bg-teal-500 h-[30px] w-[70px] text-white"
																>
																	확인
																</button>
															</div>
														</div>
													</div>
													<div
														v-else
														@click="
															openScheduleMod(
																index,
																scheduleIndex,
															)
														"
														class="w-[700px] border border-gray-200 rounded-xl px-5 py-4 bg-white flex justify-between"
														:class="{
															'border-yellow-400!':
																isEditingSchedule(
																	plan.id,
																	schedule.id,
																),
														}"
													>
														<div
															class="flex gap-4 overflow-hidden"
														>
															<div
																class="w-6 h-6"
															></div>

															<div
																class="w-[600px]"
															>
																<div
																	class="flex items-center gap-3"
																>
																	<span
																		:class="
																			scheduleCategoryMap[
																				schedule.type as ScheduleType
																			]
																				?.activeClass ||
																			''
																		"
																	>
																		<font-awesome-icon
																			:icon="
																				scheduleCategoryMap[
																					schedule.type as ScheduleType
																				]
																					?.icon ||
																				''
																			"
																			:class="
																				scheduleCategoryMap[
																					schedule.type as ScheduleType
																				]
																					?.iconClass ||
																				''
																			"
																		/>
																		{{
																			scheduleCategoryMap[
																				schedule.type as ScheduleType
																			]
																				? scheduleCategoryMap[
																						schedule.type as ScheduleType
																					]
																						?.label ||
																					""
																				: "카테고리를 선택해주세요."
																		}}
																	</span>

																	<span
																		class="text-sm text-gray-400"
																	>
																		{{
																			getTimeDiff(
																				schedule.startTime,
																				schedule.endTime,
																			)
																		}}
																	</span>

																	<span
																		class="px-2 py-1 bg-gray-100 rounded-full text-xs text-gray-500"
																	>
																		{{
																			schedule.cost.toLocaleString() ??
																			0
																		}}원
																	</span>
																</div>

																<div
																	class="mt-2 font-bold"
																>
																	<font-awesome-icon
																		:icon="[
																			'far',
																			'bookmark',
																		]"
																		class="text-gray-400 text-xs"
																	/><span
																		class="pl-1"
																		>{{
																			schedule.title
																				? schedule.title
																				: "무엇을 하는 일정인가요?"
																		}}</span
																	>
																</div>

																<div
																	class="mt-2 text-sm text-gray-500"
																>
																	<font-awesome-icon
																		:icon="[
																			'fas',
																			'location-dot',
																		]"
																		class="text-red-400 text-xs"
																	/>
																	<span
																		class="pl-1"
																		>{{
																			schedule
																				.place
																				.name ??
																			"어디로 갈건가요?"
																		}}</span
																	>
																</div>

																<div
																	class="mt-1 text-sm text-gray-400 flex"
																>
																	<font-awesome-icon
																		:icon="[
																			'far',
																			'note-sticky',
																		]"
																		class="text-xs"
																	/>
																	<div
																		class="pl-1"
																	>
																		<pre>{{
																			schedule.memo
																				? schedule.memo
																				: "예약 정보 또는 간단히 가볼 곳을 메모해보세요."
																		}}</pre>
																	</div>
																</div>
															</div>
														</div>

														<div class="relative">
															<div
																@click.stop="
																	toggleSchedulePlanMenu(
																		index,
																		scheduleIndex,
																	)
																"
																class="hover:bg-gray-100 cursor-pointer rounded-md"
															></div>
														</div>
													</div>
												</div>
											</template>
										</draggable>
										<div
											v-if="toggleDayPlanRecord[plan.id]"
											class="relative pl-[115px] mt-2 mb-2"
										>
											<div
												class="absolute left-[54px] top-0 bottom-0 w-px bg-gray-200"
											></div>
											<div
												@click="addSchedule(plan.id)"
												class="text-gray-400 w-[90px] pt-3 cursor-pointer hover:text-teal-400"
											>
												<font-awesome-icon
													:icon="[
														'far',
														'circle-xmark',
													]"
													class="rotate-45"
												/>
												일정 추가
											</div>
										</div>
									</div>
								</template>
							</draggable>
						</div>
						<div
							@click="addDay"
							class="mt-10 w-full border-dashed border-gray-300 cursor-pointer rounded-xl hover:border-teal-300 hover:text-teal-400 text-gray-400 border-2 text-center h-[50px] content-center"
						>
							<font-awesome-icon :icon="['fas', 'plus']" />
							새 일자 추가
						</div>
					</div>
				</div>
			</div>
			<invite-modal
				v-if="isInviteModalOpen"
				@close="isInviteModalOpen = false"
			></invite-modal>
			<update-travel-info-modal
				v-if="isTravelUpdateModalOpen"
				:planInfo="planInfo"
				@close="closeTravelUpdateModal"
			></update-travel-info-modal>
		</div>
	</div>
</template>
<script setup lang="ts">
import { nextTick, onMounted, onUnmounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import draggable from "vuedraggable";
import { Korean } from "flatpickr/dist/l10n/ko.js";
import flatPickr from "vue-flatpickr-component";

import * as Y from "yjs";

import {
	ScheduleType,
	type DayInfo,
	type PlanInfo,
	type Schedule,
} from "./types/PlanInfo";
import { formatToFullDateWithDay, getTimeDiff } from "@/utils/DateUtil";
import axios, { AxiosError, type AxiosResponse } from "axios";
import { useSpinnerStore } from "@/stores/Spinner";
import type { PlanInfoResponse } from "./types/response/PlanInfoResponse";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import type { ConnectedUser } from "./types/ConnectedUser";
import { useAuthStore } from "@/stores/Auth";
import type { PlanInfoUpdateRequest } from "./types/request/PlanInfoRequest";
import InviteModal from "./modal/InviteModal.vue";
import {
	scheduleCategories,
	scheduleCategoryMap,
} from "./constants/scheduleCategory.ts";
import UpdateTravelInfoModal from "./modal/UpdateTravelInfoModal.vue";
import type { EditingUser } from "./types/EditingUser.ts";
import type { IndicatorPositions } from "./types/IndicatorPositions.ts";
import { getImageSrc } from "@/utils/ImageUtil.ts";

const route = useRoute();
const auth = useAuthStore();
const { startSpinner, endSpinner } = useSpinnerStore();

// 일자 flat pickr 설정
const dayConfig = ref({
	dateFormat: "Y-m-d",
	altFormat: "Y년 m월 d일 l",
	altInput: true,
	wrap: true,
	clickOpens: true,
	allowInput: true,
	disableMobile: true,
	static: false,
	appendTo: document.body,
	locale: Korean,
});
// 스케줄 flat pickr 설정
const scheduleConfig = ref({
	enableTime: true, //  시간 선택 활성화
	noCalendar: true, // 달력(날짜 선택) 숨기기
	dateFormat: "H:i", // 실제 데이터 포맷 (H: 24시간제 시, i: 분)
	minuteIncrement: 10, // 10분 단위로 선택
	time_24hr: true,
	altInput: true,
	altFormat: "H:i", // 화면 표시 포맷 (h: 12시간제 시, K: AM/PM 표시)
});
const dayPlanMenuOpenIndex = ref(-1); // 일별 수정 삭제 메뉴 오픈 인덱스 -1이면 아무것도 열지 않음
const dayPlanModiOpenIndex = ref(-1); // 일별 수정 오픈 인덱스 -1이면 아무것도 열지 않음

// 스케줄 수정 삭제 메뉴 오픈 인덱스 -1이면 아무것도 열지 않음
const scheduleMenuOpenIndex = ref({
	dayIndex: -1,
	scheduleIndex: -1,
});
// 스케줄 수정 오픈 인덱스 -1이면 아무것도 열지 않음
const schedulePlanModiOpenIndex = ref({
	dayIndex: -1,
	scheduleIndex: -1,
});
const calCost = ref(0);
const dayInfoList = ref<DayInfo[]>([]);
// 여행 계획 동시 편집을 위한 yjs 사용
const yDoc = new Y.Doc();

// 일정별 계획 목록
const yPlans = yDoc.getArray<Y.Map<any>>("plans");

// textarea ref 변수
const textareaRef = ref<HTMLTextAreaElement | null>(null);

// 여행 정보
const planInfo = ref<PlanInfo>();

// 일정 접기 여부
const toggleDayPlanRecord = ref<Record<string, boolean>>({});

// 접속자 정보
const connectedUsers = ref<ConnectedUser[]>([]);

// 다른 사용자 조회 수정 정보
// const editingUsers = ref<Record<string, EditingUser>>({});
// 다른 사용자 위치 정보
const indicatorPositions = ref<Record<string, IndicatorPositions>>({});

const planContainer = ref<HTMLElement | null>(null);
// 초대 모달 오픈 여부
const isInviteModalOpen = ref<boolean>(false);
// 여행 정보 수정 모달 오픈 여부
const isTravelUpdateModalOpen = ref<boolean>(false);

// vue 반응성을 위해 doc 변경 시 객체 자체를 변경
const syncPlans = () => {
	const plans = yPlans.toJSON() as DayInfo[];

	dayInfoList.value = plans
		.sort((a, b) => a.order - b.order)
		.map((day) => ({
			...day,
			schedules: [...day.schedules].sort((a, b) => a.order - b.order),
		}));

	for (const day of dayInfoList.value) {
		if (toggleDayPlanRecord.value[day.id] === undefined) {
			toggleDayPlanRecord.value[day.id] = true;
		}
	}

	requestSave();
	updateCostChart();
};
// 카테고리별 총 비용 계산
interface CostChartItem {
	label: string;
	cost: number;
	percent: number;
	bgClass: string;
}

const costChartList = ref<CostChartItem[]>([]);

const updateCostChart = () => {
	let moveCost = 0;
	let hotelCost = 0;
	let foodCost = 0;
	let tourCost = 0;
	let otherCost = 0;

	for (const day of dayInfoList.value) {
		for (const schedule of day.schedules) {
			const cost = Number(schedule.cost) || 0;

			switch (schedule.type) {
				case ScheduleType.MOVE:
					moveCost += cost;
					break;

				case ScheduleType.HOTEL:
					hotelCost += cost;
					break;

				case ScheduleType.FOOD:
					foodCost += cost;
					break;

				case ScheduleType.TOUR:
					tourCost += cost;
					break;

				default:
					otherCost += cost;
					break;
			}
		}
	}

	const totalCost = moveCost + hotelCost + foodCost + tourCost + otherCost;

	const getPercent = (cost: number) => {
		if (totalCost === 0) return 0;

		return (cost / totalCost) * 100;
	};

	costChartList.value = [
		{
			label: "교통",
			cost: moveCost,
			percent: getPercent(moveCost),
			bgClass: "bg-sky-300",
		},
		{
			label: "숙박",
			cost: hotelCost,
			percent: getPercent(hotelCost),
			bgClass: "bg-indigo-300",
		},
		{
			label: "식비",
			cost: foodCost,
			percent: getPercent(foodCost),
			bgClass: "bg-orange-300",
		},
		{
			label: "관광",
			cost: tourCost,
			percent: getPercent(tourCost),
			bgClass: "bg-teal-300",
		},
		{
			label: "기타",
			cost: otherCost,
			percent: getPercent(otherCost),
			bgClass: "bg-purple-300",
		},
	];
};
const updateDayOrder = () => {
	yDoc.transact(() => {
		dayInfoList.value.forEach((day, index) => {
			const yDay = yPlans
				.toArray()
				.find((sub) => sub.get("id") === day.id);
			if (!yDay) return;

			yDay.set("order", index);
		});
	}, "sort");
};
const updateScheduleOrder = (planId: string, schedules: Schedule[]) => {
	yDoc.transact(() => {
		schedules.forEach((schedule, index) => {
			const ySchedule = getYSchedule(planId, schedule.id);

			if (!ySchedule) return;

			ySchedule.set("order", index);
		});
	}, "sort");
};
// 새로운 일정 추가
const addDay = () => {
	const yDay = new Y.Map<any>();

	yDay.set("id", crypto.randomUUID());
	yDay.set("title", "");
	yDay.set("date", null);
	yDay.set("order", yPlans.length);

	const ySchedules = new Y.Array<Y.Map<any>>();
	yDay.set("schedules", ySchedules);

	yPlans.push([yDay]);
	syncPlans();
};

const addSchedule = (id: string) => {
	const yMap = new Y.Map<any>();
	const yPlaceMap = new Y.Map<any>();
	yMap.set("id", crypto.randomUUID());
	yMap.set("title", "");
	yMap.set("type", null);

	yPlaceMap.set("name", null);
	yMap.set("place", yPlaceMap);
	yMap.set("memo", "");
	yMap.set("cost", 0);
	yMap.set("startTime", "00:00");
	yMap.set("endTime", "00:00");

	// 해당 일자 doc 추출
	const yDay = yPlans.toArray().find((day) => day.get("id") === id);

	if (!yDay) {
		return;
	}
	// 해당 일자의 스케줄 배열 추출
	const ySchedules = yDay.get("schedules") as Y.Array<Y.Map<any>>;
	yMap.set("order", ySchedules.length);

	// 스케줄 추가
	ySchedules.push([yMap]);

	syncPlans();
};

// 하루 총 예상 경비 반환
const getDayCost = (schedules: Schedule[]) => {
	let totalCost = 0;
	schedules.forEach((schedule) => {
		totalCost += schedule.cost;
	});
	return totalCost.toLocaleString();
};
const getPlan = () => {
	startSpinner();
	axios
		.get(`/web/api/travel/plan/${route.params.id}`)
		.then((res: AxiosResponse<PlanInfoResponse>) => {
			planInfo.value = res.data.planInfo;
			if (isFirst.value) {
				dayInfoList.value = res.data.planDetails;
				for (const day of dayInfoList.value) {
					if (toggleDayPlanRecord.value[day.id] === undefined) {
						toggleDayPlanRecord.value[day.id] = true;
					}
				}
				isFirst.value = false;
				updateCostChart();
				initYDoc();
			}
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
		})
		.finally(() => endSpinner());
};
// db에서 조회한 스케줄 yDoc에 세팅
const initYDoc = () => {
	yDoc.transact(() => {
		// 기존 데이터 제거
		if (yPlans.length > 0) {
			yPlans.delete(0, yPlans.length);
		}

		for (const plan of dayInfoList.value) {
			const yPlan = new Y.Map<any>();

			yPlan.set("id", plan.id);
			yPlan.set("title", plan.title);
			yPlan.set("date", plan.date);

			// 일정 목록
			const ySchedules = new Y.Array<Y.Map<any>>();

			for (const schedule of plan.schedules ?? []) {
				const ySchedule = new Y.Map<any>();

				ySchedule.set("id", schedule.id);
				ySchedule.set("type", schedule.type);
				ySchedule.set("title", schedule.title);
				ySchedule.set("memo", schedule.memo ?? "");
				ySchedule.set("cost", schedule.cost ?? 0);
				ySchedule.set("startTime", schedule.startTime ?? "");
				ySchedule.set("endTime", schedule.endTime ?? "");

				// 장소
				if (schedule.place) {
					const yPlace = new Y.Map<any>();

					yPlace.set("name", schedule.place.name);
					yPlace.set("name", schedule.place.name ?? "");
					yPlace.set("lat", schedule.place.latitude ?? null);
					yPlace.set("lng", schedule.place.longitude ?? null);
					yPlace.set("placeId", schedule.place.placeId ?? "");

					ySchedule.set("place", yPlace);
				}

				ySchedules.push([ySchedule]);
			}

			yPlan.set("schedules", ySchedules);

			yPlans.push([yPlan]);
		}
	}, "init");
};

// 비용 계산
const calculateCost = (schedule: Schedule, cal: string, planId: string) => {
	if (cal === "plus") {
		schedule.cost += calCost.value;
	} else {
		schedule.cost -= calCost.value;
	}
	calCost.value = 0;

	const ySchedule = getYSchedule(planId, schedule.id);

	if (!ySchedule) {
		return;
	}

	// yDoc 업데이트
	ySchedule.set("cost", schedule.cost);
	updateCostChart();
	requestSave();
};

// 일정 제목 수정 - index: 일자 배열 index, id: 일자 id
const planTitleInput = (event: Event, index: number, id: string) => {
	const target = event.target as HTMLInputElement;
	target.value = target.value.substring(0, 30);
	if (target.value.length)
		// 화면 반응성을 위한 변수 수정
		if (dayInfoList.value && dayInfoList.value[index]) {
			dayInfoList.value[index].title = target.value;
		}

	// 해당 일자 doc 추출
	const yDay = yPlans.toArray().find((day) => day.get("id") === id);

	if (!yDay) {
		return;
	}

	// 일정 제목 변경
	yDay.set("title", target.value);

	requestSave();
};
// 일정 날짜 yDoc 수정 - index: 일자 배열 index, 일자 id
const planDateInput = (index: number, id: string) => {
	// 해당 일자 doc 추출
	const yDay = yPlans.toArray().find((day) => day.get("id") === id);

	if (!yDay) {
		return;
	}
	if (!(dayInfoList.value && dayInfoList.value[index])) {
		return;
	}

	// 일정 날짜 변경
	yDay.set("date", dayInfoList.value[index].date);

	requestSave();
};

// 스케줄 시간 변경
const updateScheduleTime = (
	dateStr: string,
	planId: string,
	scheduleId: string,
	field: string,
) => {
	// 해당 일자의 스케줄 목록에서 수정한 스케줄 추출
	const ySchedule = getYSchedule(planId, scheduleId);

	if (!ySchedule) {
		return;
	}

	ySchedule.set(field, dateStr);

	requestSave();
};
// 일정 삭제
const deleteDayPlan = (planId: string, planIndex: number) => {
	if (!confirm(planIndex + 1 + "일차 일정을 정말 삭제하시겠습니까?")) {
		return;
	}
	const index = yPlans.toArray().findIndex((day) => day.get("id") === planId);

	if (index !== -1) {
		yPlans.delete(index, 1);
	}

	dayInfoList.value.splice(planIndex, 1);
	dayPlanMenuOpenIndex.value = -1;

	requestSave();
};
// 일정 삭제
const deleteSchedule = (
	planId: string,
	planIndex: number,
	scheduleId: string,
	scheduleIndex: number,
) => {
	if (!confirm(scheduleIndex + 1 + "번째 일정을 정말 삭제하시겠습니까?")) {
		return;
	}
	const dayPlan = yPlans.toArray().find((day) => day.get("id") === planId);

	if (!dayPlan) {
		return;
	}
	const index = dayPlan
		.get("schedules")
		.toArray()
		.findIndex((schedule: Y.Map<any>) => schedule.get("id") === scheduleId);

	if (index === -1) {
		return;
	}
	dayPlan.get("schedules").delete(index, 1);

	dayInfoList.value[planIndex]?.schedules.splice(scheduleIndex, 1);

	schedulePlanModiOpenIndex.value.dayIndex = -1;
	schedulePlanModiOpenIndex.value.scheduleIndex = -1;
	requestSave();
};
// 스케줄 수정
const updateSchedule = (
	event: Event,
	planId: string,
	scheduleId: string,
	field: string,
) => {
	const target = event.target as HTMLInputElement;

	const ySchedule = getYSchedule(planId, scheduleId);

	if (!ySchedule) {
		return;
	}

	// place 내부 필드
	if (field === "name") {
		const yPlace = ySchedule.get("place") as Y.Map<any>;

		if (yPlace) {
			yPlace.set("name", target.value);
		}

		return;
	}

	// 일반 schedule 필드
	ySchedule.set(field, target.value);

	if (field === "memo") {
		adjustHeight();
	} else if (field === "cost") {
		updateCostChart();
	}

	requestSave();
};
const openScheduleMod = (index: number, scheduleIndex: number) => {
	schedulePlanModiOpenIndex.value.dayIndex = index;
	schedulePlanModiOpenIndex.value.scheduleIndex = scheduleIndex;

	adjustHeight();
};
// 스케줄 카테고리 선택
const updateScheduleCategory = (
	type: ScheduleType,
	planId: string,
	scheduleId: string,
) => {
	// 해당 일자의 스케줄 목록에서 수정한 스케줄 추출
	const ySchedule = getYSchedule(planId, scheduleId);

	if (!ySchedule) {
		return;
	}
	ySchedule.set("type", type);

	requestSave();
};

// yDoc에서 스케줄 반환
const getYSchedule = (planId: string, scheduleId: string) => {
	// 해당 일자 doc 추출
	const yDay = yPlans.toArray().find((day) => day.get("id") === planId);
	if (!yDay) {
		return null;
	}

	// 해당 일자의 스케줄 추출
	const ySchedules = yDay.get("schedules") as Y.Array<Y.Map<any>>;
	// 해당 일자의 스케줄 목록에서 수정한 스케줄 추출
	const ySchedule = ySchedules
		.toArray()
		.find((schedule) => schedule.get("id") === scheduleId);
	if (!ySchedule) {
		return null;
	}
	return ySchedule;
};

// 일자 수정 삭제 메뉴 여닫기
const toggleDayPlanMenu = (index: number) => {
	if (dayPlanMenuOpenIndex.value === -1) {
		dayPlanMenuOpenIndex.value = index;
		return;
	}
	dayPlanMenuOpenIndex.value = -1;
};
// 스케줄 수정 삭제 메뉴 여닫기
const toggleSchedulePlanMenu = (dayIndex: number, scheduleIndex: number) => {
	if (
		scheduleMenuOpenIndex.value.dayIndex !== -1 ||
		scheduleMenuOpenIndex.value.scheduleIndex !== -1
	) {
		scheduleMenuOpenIndex.value.dayIndex = -1;
		scheduleMenuOpenIndex.value.scheduleIndex = -1;
		return;
	}
	scheduleMenuOpenIndex.value.dayIndex = dayIndex;
	scheduleMenuOpenIndex.value.scheduleIndex = scheduleIndex;
};

// 메모 textarea 스크롤 없이 입력할 때마다 한번에 다 보이게 설정
const adjustHeight = async () => {
	await nextTick();
	if (textareaRef.value) {
		textareaRef.value.style.height = "auto";
		textareaRef.value.style.height = `${textareaRef.value.scrollHeight}px`;
	}
};

// 총 경비 반환
const totalCost = () => {
	let res = 0;
	dayInfoList.value.forEach((plan) => {
		plan.schedules.forEach((schedule) => {
			res += schedule.cost;
		});
	});
	return res.toLocaleString();
};
// 메뉴 바깥 클릭 시 메뉴 닫기
const handleBodyClick = () => {
	dayPlanMenuOpenIndex.value = -1;
	scheduleMenuOpenIndex.value.dayIndex = -1;
	scheduleMenuOpenIndex.value.scheduleIndex = -1;
	schedulePlanModiOpenIndex.value.dayIndex = -1;
	schedulePlanModiOpenIndex.value.scheduleIndex = -1;
};

let socket: WebSocket | null = null;
const YJS_UPDATE = 0; // 스케줄 데이터 변경
const AWARENESS = 1; // 누가 어디를 수정/선택했는지
const PRESENCE = 2; // 접속/참여 사용자
const INIT = 3; // 다음에 들어온 참가자에게 yDoc 전달용 타입
const isFirst = ref(true); // 여행 계획에 처음 진입 여부
// webSocket 연결
const connectWebSocket = () => {
	socket = new WebSocket(`ws://localhost:8080/ws/plan/${route.params.id}`);

	socket.binaryType = "arraybuffer";

	socket.onopen = () => {
		console.log("WebSocket 연결 성공");
	};

	yDoc.on("update", (update, origin) => {
		if (origin === "remote" || origin === "init") {
			return;
		}
		if (origin === "sort") {
			requestSave();
		}
		const message = new Uint8Array(update.length + 1);

		message[0] = YJS_UPDATE;
		message.set(update, 1);

		if (socket && socket.readyState === WebSocket.OPEN) {
			socket.send(message);
		}
	});

	socket.onmessage = (event) => {
		const message = new Uint8Array(event.data);

		const type = message[0]; // 1번째 바이트 값 추출
		const payload = message.slice(1); // 1번째 바이트 건너뛰고 다음거만 추출

		switch (type) {
			case YJS_UPDATE:
				Y.applyUpdate(yDoc, payload, "remote");
				syncPlans();

				break;
			case AWARENESS:
				const awarenessJson = new TextDecoder().decode(payload);
				const info = JSON.parse(awarenessJson);

				// planId 와 scheduleId로 위치 정보 설정
				updateIndicatorPosition(info);
				break;
			case PRESENCE:
				const usersJson = new TextDecoder().decode(payload);
				const users = JSON.parse(usersJson);

				connectedUsers.value = users;
				if (connectedUsers.value.length > 1) {
					sendFullYDoc(); // 새로운 참가자 존재 시 yDoc provider가 자신의 yDoc 전송
				} else {
					// 최초 진입 여부
					getPlan(); // 나만 이 방에 있다면 db에서 조회
				}
				break;
			case INIT: // YJS_UPDATE와 동일하지만 서버에서 구분하기 위한 값
				Y.applyUpdate(yDoc, payload, "remote");
				syncPlans();
				break;
		}
	};

	socket.onclose = () => {
		console.log("WebSocket 연결 종료");
	};

	socket.onerror = (error) => {
		console.error("WebSocket 에러:", error);
	};
};

const sendFullYDoc = () => {
	const myInfo = connectedUsers.value.find(
		(user) => user.userId === auth.getUserId(),
	);

	// 자신이 싱크 기준점 사용자라면 새로운 참가자에게 yDoc 전송
	if (myInfo?.syncProvider) {
		const update = Y.encodeStateAsUpdate(yDoc);

		const message = new Uint8Array(update.length + 1);

		message[0] = INIT;
		message.set(update, 1);

		if (socket && socket.readyState === WebSocket.OPEN) {
			socket.send(message);
		}
	}
};

const sendMyIndicator = (
	event: MouseEvent,
	planId: string,
	scheduleId: string | null,
) => {
	if (!socket || socket.readyState !== WebSocket.OPEN) {
		return;
	}
	const target = event.target as HTMLElement;

	const isEditing = !!target.closest('[data-is-mod="true"]');
	const sendInfo: EditingUser = {
		userId: auth.getUserId(),
		nickName: auth.getNickName(),
		planId,
		scheduleId,
		isEditing,
	};
	const payload = new TextEncoder().encode(JSON.stringify(sendInfo));
	const message = new Uint8Array(payload.length + 1);
	message[0] = AWARENESS; // 위치 정보 타입
	message.set(payload, 1);
	socket.send(message);
};

// 현재 조회, 수정중인 일정 또는 스케줄인지 반환
const isEditingSchedule = (planId: string, scheduleId: string | null) => {
	for (const [key, value] of Object.entries(indicatorPositions.value)) {
		if (value.planId === planId && value.scheduleId === scheduleId) {
			return true;
		}
	}
	return false;
};
const updateIndicatorPosition = (user: EditingUser) => {
	if (!planContainer.value) return;

	let element;
	if (user.scheduleId) {
		element = planContainer.value.querySelector<HTMLElement>(
			`[data-schedule-id="${user.scheduleId}"]`,
		);
		// 중간에 접기해서 스케줄 dom이 사라졌을 때 일정으로 이동
		if (!element) {
			element = planContainer.value.querySelector<HTMLElement>(
				`[data-plan-id="${user.planId}"]`,
			);
		}
	} else {
		element = planContainer.value.querySelector<HTMLElement>(
			`[data-plan-id="${user.planId}"]`,
		);
	}

	if (!element) {
		delete indicatorPositions.value[user.userId];
		return;
	}

	const containerRect = planContainer.value.getBoundingClientRect();

	const scheduleRect = element.getBoundingClientRect();
	let top = scheduleRect.top - containerRect.top;
	for (const [key, value] of Object.entries(indicatorPositions.value)) {
		// 같은걸 보고 있는 사용자 존재 시, 기존 사용자 밑에 생기게 계산
		if (
			value.userId !== user.userId && // 내가 다른 탭에서 보고 있는건 제외
			value.planId === user.planId &&
			value.scheduleId === user.scheduleId
		) {
			top += 30;
		}
	}
	indicatorPositions.value[user.userId] = {
		// 스케줄 오른쪽 위
		userId: user.userId,
		nickName: user.nickName,
		planId: user.planId,
		scheduleId: user.scheduleId,
		top,
		left: scheduleRect.right - containerRect.left + 8,
		visible: true,
		isEditing: user.isEditing,
	};
};

// 일정 접기 펼치기 시 다른 사용자 위치 재갱신
const refreshIndicatorPosition = async (planId: string) => {
	toggleDayPlanRecord.value[planId] = !toggleDayPlanRecord.value[planId];
	await nextTick();
	for (const [key, value] of Object.entries(indicatorPositions.value)) {
		const editingUser: EditingUser = {
			userId: value.userId,
			nickName: value.nickName,
			planId: value.planId,
			scheduleId: value.scheduleId,
			isEditing: value.isEditing,
		};
		updateIndicatorPosition(editingUser);
	}
};

let saveTimer: ReturnType<typeof setTimeout> | null = null;

// 3초 동안 추가 수정이 없으면 DB 저장
const requestSave = () => {
	const myInfo = connectedUsers.value.find(
		(user) => user.userId === auth.getUserId(),
	);

	// 자신이 싱크 기준점 사용자라면 새로운 참가자에게 yDoc 전송
	if (!myInfo?.syncProvider) {
		return;
	}
	if (saveTimer) {
		clearTimeout(saveTimer);
	}

	saveTimer = setTimeout(() => {
		savePlan();
		saveTimer = null;
	}, 3000);
};

// 여행 계획 저장
const savePlan = () => {
	if (!dayInfoList.value) {
		return;
	}
	const request: PlanInfoUpdateRequest = {
		dayInfoList: dayInfoList.value,
	};

	axios
		.put(`/web/api/travel/plan/${route.params.id}`, request)
		.then()
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
		});
};

const closeTravelUpdateModal = (isRefresh: boolean) => {
	if (isRefresh) {
		getPlan();
	}
	isTravelUpdateModalOpen.value = false;
};
onMounted(() => {
	window.addEventListener("click", handleBodyClick);
	connectWebSocket();
});
onUnmounted(() => {
	socket?.close();
});
</script>
<style scope>
.editing-indicator {
	position: absolute;

	padding: 4px 8px;

	background: #facc15;
	border-radius: 6px;

	font-size: 12px;
	font-weight: 500;

	white-space: nowrap;

	transition:
		top 0.25s ease,
		left 0.25s ease;

	z-index: 20;
}
</style>
