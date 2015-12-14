/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.sample.comics.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.domain.usecase.GetComicDetails;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicDetailsViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.mapper.ComicToComicDetailsViewModelMapper;
import com.karumi.rosie.view.loading.RosiePresenterWithLoading;
import javax.inject.Inject;

public class ComicDetailsPresenter extends RosiePresenterWithLoading<ComicDetailsPresenter.View> {

  private final GetComicDetails getComicDetails;
  private final ComicToComicDetailsViewModelMapper mapper;
  private int comicKey;

  @Inject
  public ComicDetailsPresenter(UseCaseHandler useCaseHandler, GetComicDetails getComicDetails,
      ComicToComicDetailsViewModelMapper mapper) {
    super(useCaseHandler);
    this.getComicDetails = getComicDetails;
    this.mapper = mapper;
  }

  public void setComicKey(int comicKey) {
    this.comicKey = comicKey;
  }

  @Override protected void update() {
    super.update();
    showLoading();
    getView().hideComicDetails();
    loadComicDetails();
  }

  private void loadComicDetails() {
    createUseCaseCall(getComicDetails).args(comicKey).onSuccess(new OnSuccessCallback() {
      @Success public void onComicDetailsLoaded(Comic comic) {
        ComicDetailsViewModel comicDetailsViewModel =
            mapper.mapComicToComicDetailsViewModel(comic);
        hideLoading();
        getView().showComicDetails(comicDetailsViewModel);
      }
    }).execute();
  }

  public interface View extends RosiePresenterWithLoading.View {
    void hideComicDetails();

    void showComicDetails(ComicDetailsViewModel comic);
  }
}
